package com.cs.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.cs.ad.mysql.dto.MysqlRowData;
import com.cs.ad.sender.ISender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author fucker
 * @Component("kafkaSender") 将这个bean命名为kafkaSender
 * 注入接口时，可以根据bean的名称，指定注入对应的实现类
 */
@Component("kafkaSender")
public class KafkaSender implements ISender {
   /**定义Kafka的topic，
    * 相关的配置放在配置文件中,
    * 通过注解指定配置文件的路径来 读配置*/
    @Value("${adconf.kafka.topic}")
    private String topic;

    /**注入kafkaTemplate*/
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public void sender(MysqlRowData rowData) {

        /*kafkaTemplate的send方法进行填充数据，实现增量数据投递*/
        kafkaTemplate.send(
                /*传入topic，JSON化的数据对象*/
                topic, JSON.toJSONString(rowData)
        );
    }

    /**测试方法，Kafka监听器，监听topic的消息
     * 注解通过读配置文件，实现对指定的topic进行监听
     * 指定groupId，该group才会接受到监听的消息
     * */
    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        /*如果Kafka message实例存在就去消费*/
        if (kafkaMessage.isPresent()) {
            /*获取到message实例*/
            Object message = kafkaMessage.get();
            /*将message中的数据反序列化为RowData对象*/
            MysqlRowData rowData = JSON.parseObject(
                    message.toString(),
                    MysqlRowData.class
            );
            /*测试，打印rowData数据*/
            System.out.println("kafka processMysqlRowData: " +
                    JSON.toJSONString(rowData));
        }
    }
}
