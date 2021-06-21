package com.cs.ad.mysql.Binlog;

import com.cs.ad.mysql.listener.AggregationListener;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author fucker
 * 实现启动连接，监听binlog
 */
@Slf4j
public class BinlogClient {


    /**   定义client*/
    private BinaryLogClient client;

    /**注入配置类*/
    @Autowired
    private BinlogConfig config;
    /**注入监听器*/
    @Autowired
    private AggregationListener listener;

    /**使用connect开始监听，程序启动时就开始监听，
     * 需要实现springBoot中的接口
     *
     * 通过调用connect方法实现监听器的启动
    * 但是使用connect方法后，现在就会阻塞，一直保持连接状态
    * 监听还需要其他线程来实现.
     * 新建现场执行connect操作，主线程执行其他操作*/

    public void connect(){
//        在线程中实现方法
        new Thread(() ->{
//            完成相关的配置
            client = new  BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
//            如果binLogName不为空，且Position()不为-1，则代表其有效,可以开始监听
            if (!StringUtils.isEmpty(config.getBinlogName())
                    && !config.getPosition().equals(-1L)){

//                给client填充值，开始监听
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
//            传入listener，注册事件监听器
            client.registerEventListener(listener);
            try {
                log.info("connecting to mysql start");
                client.connect();
                log.info("connecting to mysql done");
//                connect方法可能会抛出异常，需要捕获并打印异常栈
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**断开MySQL的监听*/
    public void close(){
        try {
            /*使用disconnect()断开*/
            client.disconnect();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
