package com.cs.ad.mysql.listener;

import com.cs.ad.mysql.constant.Constant;
import com.cs.ad.mysql.constant.OpType;
import com.cs.ad.mysql.dto.BinlogRowData;
import com.cs.ad.mysql.dto.MysqlRowData;
import com.cs.ad.mysql.dto.TableTemplate;
import com.cs.ad.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 * 增量数据的处理器
 */
@Slf4j
@Component
public class IncrementListener implements Ilistener {

    /**根据name选择注入
     * sender是投递接口，其中有多个投递方式
     * name选择特定的实现方式*/
    @Resource(name = "")
    private ISender sender;
    /**需要使用AggregationListener实现注册*/
    @Autowired
    private AggregationListener aggregationListener;

    /**根据binlog处理完后实现的listener
     * 放入spring容器时就完成注册,
     * 不然就找不到listener直接丢弃*/
    @Override
    @PostConstruct
    public void register() {
        log.info("Register");
        Constant.table2Db.forEach((k,v) ->
//      v为数据库名，k为数据表名，this为IncrementListener
        aggregationListener.register(v,k,this));

    }

    /**将BinlogRowData转化为MySQLRowData
     * 并投递出去*/
    @Override
    public void onEvent(BinlogRowData eventData) {
//        获取到BinlogRowData中嵌套的table对象
        TableTemplate table = eventData.getTable();
//        获取到 eventType
        EventType eventType = eventData.getEventType();

//        包装成最后需要投递的数据,new出MySQLRowData
        MysqlRowData rowData =  new MysqlRowData();

//        开始向MySQLRowData中填充数据
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());

//        设置OpType,OpType和eventType之间需要转换
        OpType opType = OpType.to(eventType);
//        填充opType
        rowData.setOpType(opType);

//        取出模板中该操作type对应的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
//        如果需要处理的字段列表为null
        if(null  == fieldList){
//            则打印警告日志+不处理
            log.warn("");
            return;
        }
//        遍历发生变化的列和列值,即遍历after
        for (Map<String, String> afterMap : eventData.getAfter()){
//            解析每一个遍历出来的afterMap
            Map<String, String> _afterMap = new HashMap<>();
            for(Map.Entry<String, String> entry : afterMap.entrySet()){
                String colName = entry.getKey();
                String colValue = entry.getValue();
//                解析结果填充到_afterMap
                _afterMap.put(colName, colValue);
            }
//            将解析后的map添加进fieldValueMap
            rowData.getFiledValueMap().add(_afterMap);

        }

//        最后通过是sender()将rowData投递出去
        sender.sender(rowData);
    }
}
