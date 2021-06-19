package com.cs.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author fucker
 *
 * 将开源的bingLog读取工具传入的event转化为
 * inlogRowData这个Java对象
 */
@Data
public class BinlogRowData {

    /**table和after一起被填充*/
    private TableTemplate table;

    /*血坑，这里引入了javafx包的event*/
    /**设置监听器时填充Type*/
    private EventType eventType;

    /**list中是Map，map的key对应的是操作的列的名字
     * value对应的是操作的列的值
     */
    private List<Map<String,String>> after;

    /**不关注before，因此before不填充*/
    private List<Map<String,String>> before;




}
