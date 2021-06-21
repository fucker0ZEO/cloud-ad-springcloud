package com.cs.ad.runner;

import com.cs.ad.mysql.Binlog.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {
    @Autowired
    private BinlogClient client;

    @Override
    public void run(String... args) throws Exception {
        log.info("Coming in BinlogRunner......");
        /**调用监听方法，开始监听*/
        client.connect();
    }
}
