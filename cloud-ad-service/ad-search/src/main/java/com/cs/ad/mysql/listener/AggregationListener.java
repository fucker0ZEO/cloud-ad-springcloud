package com.cs.ad.mysql.listener;

import com.cs.ad.mysql.TemplateHolder;
import com.cs.ad.mysql.dto.BinlogRowData;
import com.cs.ad.mysql.dto.TableTemplate;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fucker
 * 搜集并聚合Binlog
 *
 *
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    /*定义的一些属性*/

    private String dbName;
    private String tableName;
    /**定义处理方法
     * 同一个监听器可以多次实现
     * 注册不同的数据库以及数据表。
     * 每个数据库中每张表都可以定义不同的监听方法去处理
     * 因此定义为map
     * String对应监听的表
     * Ilistener对应处理方法*/
    private Map<String,Ilistener> ilistenerMap = new HashMap<>();

    /**注入模板方法*/
    @Autowired
    private TemplateHolder templateHolder;

    /**构造一个方法生成map的key
    * 完全标识一张表需要数据库名和表名,
     * 传入库名和表名，返回一个key*/
    private String genKey(String dbName,String tableName){
        return dbName + ":" +tableName;
    }

    /**注册监听器
     * _dbName +_tableName 代表事件源
     * Ilistener 代表事件。发生了该事件即做成响应。即发生xx操作
     * */
    public void register(String _dbName, String _tableName,Ilistener ilistener){

//        对某个表实现了listener注册,即表对应的Binlog会去投递
        log.info("");
        /*根据表/唯一标识的key，实现注册方法*/
        this.ilistenerMap.put(genKey(_dbName,_tableName),ilistener);
    }

    /**传递进来的参数event，就算Binlog中的数据
     * 将event解析为自定义的BinlogRowData
     * （最后将BinlogRowData传递给listener，实现增量数据的更新）*/
    @Override
    public void onEvent(Event event) {
//        根据event获取eventType
        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}",type);

//        TABLE_MAP中包含有要操作的数据库和数据表的名字
        if (type == EventType.TABLE_MAP){
            /*获取传递进来的event中所有的数据并填充到
            * TableMapEventDat data中
            * */
            TableMapEventData data = event.getData();

//            将data中的数据填充到本类中定义的dbName和tableName中
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();

            return;
        }

//        非以下几个类型之一，都不需要处理.EventType的类型和MySQL的版本相关
        if ( type != EventType.EXT_DELETE_ROWS
                && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS){
            return;
        }

//        校验表名和库名是否存在。两者若有一为空则不处理且打印错误日志
        if(StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)){
            log.error("no meta data event");
            return;
        }

//        找出对应表有兴趣的监听器。监听器的兴趣即ilistener这个操作类型
        String key = genKey(this.dbName,this.tableName);
//        拿到监听器的 兴趣 listener
        Ilistener listener = this.ilistenerMap.get(key);
//        如果监听器对listener不感兴趣
//        （数据库中所有的变化都会被listener记录下来，
//        但是监听器只对其中的一部分数据感兴趣，只监听一部分）
        if (null == listener){
            log.debug("skip {}",key);
            return;
        }
//        反之不为空则处理binlog,打印日志显示触发了某种事件（监听器感兴趣的事件）
        log.info("trigger event: {}",type.name());
        try {
            /*将event中的Data传递给buildRowData中的eventData
            * rowData对象，在调用buildRowData方法时，已new。返回值也是rowData
            * 返回BinlogRowData 实现转化
            * */
            BinlogRowData rowData = buildRowData(event.getData());
            /*校验获取到的rowData是否为null*/
            if(rowData == null){
//                为null直接返回
                return;
            }
//            否则做一些设置，
//            设置type为BinlogRowData实体类中的evenType
            rowData.setEventType(type);
//            传入数据，处理方法（接口&实现类）完成事件处理
            listener.onEvent(rowData);

        }catch (Exception e){
            /*打印异常堆栈*/
            e.printStackTrace();
            /*打印错误日志*/
            log.debug(e.getMessage());
        } finally {
            this.dbName = "";
            this.tableName = "";
        }

    }

    /**获取after的值,即rows，after和before
     * 的值都是一个列表[],
     * 例如 rows=[10，10，宝马]，
     * 例如 rows=[{before=[10,10,宝马]，after=[10,11,宝马]}]
     *
     * 对不同的类型获取到更新之后的统一数据
     * */
    private List<Serializable []> getAfterValues(EventData eventData) {
//      如果eventData是写入类型，则做一个强转并返回
        if (eventData instanceof WriteRowsEventData) {
//            通过getRows方法获取到对应的rows，即可[10,10,宝马]
            return ((WriteRowsEventData) eventData).getRows();
        }
//        如果eventData是更新类型
        if (eventData instanceof UpdateRowsEventData){
            //通过getRows方法获取到对应的rows，即可[10,10,宝马]
            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        //如果eventData是删除类型
        if (eventData instanceof DeleteRowsEventData) {
            //通过getRows方法获取到对应的rows，即可[10,10,宝马]
            return ((DeleteRowsEventData) eventData).getRows();
        }

//        这3类以外返回空list，代表不去处理
        return Collections.emptyList();

    }
    /**工具方法eventData转变为BinlogRowData*/
    private BinlogRowData buildRowData(EventData eventData){
//        根据tableName获取table相关信息
        TableTemplate table = templateHolder.getTable(tableName);
//        如果table获取不到
        if (table == null){
            log.warn("table {} not found",tableName);

            return null;
        }
//       定义afterMapList用于填充rowData中的after。将列的名字和列的值相关联
        List<Map<String, String>> afterMapList = new ArrayList<>();
//        for循环遍历eventData中的after的值，先填充进afterMap,
//        然后每次将Map填充afterMapList
        for (Serializable[] after : getAfterValues(eventData)) {
            Map<String, String> afterMap = new HashMap<>();

            int colLen = after.length;

            for (int ix = 0; ix < colLen; ++ix) {

                // 取出当前位置对应的列名
                String colName = table.getPositionMap().get(ix);

                // 如果没有则说明不关心这个列
                if (null == colName) {
                    log.debug("ignore position: {}", ix);
                    continue;
                }

//                获取到对应的字符串值
                String colValue = after[ix].toString();
//                填充入afterMap
                afterMap.put(colName, colValue);
            }
//            每次遍历都将afterMap填充到afterMapList中
            afterMapList.add(afterMap);
        }

        /*new rowData对象，唯一的rowData对象
        最后将afterMapList填充进rowData的after属性中
        * 最后返回rowData*/
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
//        填充table进rowData
        rowData.setTable(table);
        return rowData;
    }
}
