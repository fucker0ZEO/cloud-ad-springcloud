package com.cs.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

/**
 * @author fucker
 * 监听bingLog的配置类
 */
public class BinlogServiceTest {

    public static void main(String[] args) throws IOException {
        /*传入mysql的客户端相关参数*/
         BinaryLogClient client = new BinaryLogClient(
                 "127.0.0.1",
                 3306,
                 "root",
                 "root"

         );
         client.setBinlogFilename("binlog.00027");
         /*可以设计从那个位置开始监听binlog,默认是末尾开始监听*/

        /*注册事件监听器*/
        client.registerEventListener(event -> {
             EventData data = event.getData();
             /*是否发生了更新*/
             if (data instanceof UpdateRowsEventData){
                 System.out.println("update___________________");
                 System.out.println(data.toString());
                 /*是否发生了写入*/
             }else if (data instanceof WriteRowsEventData){
                 System.out.println("Write___________________");
                 System.out.println(data.toString());
                 /*是否发生了更新*/
             }else if (data instanceof DeleteRowsEventData){
                 System.out.println("Delete___________________");
                 System.out.println(data.toString());
             }
        });

        /*连接到MySQL，开启监听*/
        client.connect();

    }
}
