package com.cs.ad.mysql.dto;

import com.cs.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author fucker
 * 结合另外几个模板，完整的表达整个模板文件
 */
@Data
public class ParseTemplate {

    /**表示模板文件中声明的数据库*/
    private String database;

    /**对表中所有属性的表达*/
    private Map<String,TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template _template){

         ParseTemplate template = new ParseTemplate();
         /*填充template*/
         template.setDatabase(_template.getDatabase());

         /*遍历_template*/
        for(JsonTable table : _template.getTableList()){
            /*获取table的名字*/
            String name = table.getTableName();
            /*获取table的层级*/
            Integer level = table.getLevel();

            /*每一张表都new一个table*/
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            /*原本的level是Integer*/
            tableTemplate.setLevel(level.toString());
            /*将<表名，{表名，层级}> 填充到map中*/
            template.tableTemplateMap.put(name,tableTemplate);
            /*遍历操作类型对应的列*/
            Map<OpType, List<String>> opTypeFieldSetMap =
                    tableTemplate.getOpTypeFieldSetMap();

            /*依次遍历insert，update，delete中的Column*/
            for (JsonTable.Column column : table.getInsert()){
                getAndCreateIfNeed(
                        OpType.ADD,
                        opTypeFieldSetMap,
                        ArrayList:: new
                        /*最后完成添加insert支持的列*/
                ).add(column.getColumn());
            }

            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
        }
        /*返回template*/
        return template;

    }

    /**添加各个列的工具方法
     * 如果传入的操作类型（也就是key）在map中不存在
     * 则new一个arrayList作为map中的value，
     * 传入的操作类型作为key，put到map中，
     * 并返回value，
     * 如果存在则直接返回arrayList
     * */
    private static <T,R> R getAndCreateIfNeed(T key, Map<T,R> map,
                                              Supplier<R> factory){
        return map.computeIfAbsent(key,k -> factory.get());
    }

}
