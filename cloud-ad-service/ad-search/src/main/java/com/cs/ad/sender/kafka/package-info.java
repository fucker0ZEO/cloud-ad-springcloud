package com.cs.ad.sender.kafka;

/*将增量数据RowData,序列化为JSON，投放到kafka中
* 其他服务通过监听Kafka的topic中的消息，获取到message，即数据。
* 这是有别于投递到本地索引的另一种投递方式，
* 可以实现跨服务传递数据
* */