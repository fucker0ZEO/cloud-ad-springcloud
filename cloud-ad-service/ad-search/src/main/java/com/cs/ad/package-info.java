package com.cs.ad;

/*整体步骤梳理
*
* 正向/倒排索引对象的建立
* 全量索引的加载
* （DB中的所需要的记录，导出为JSON文件，
* 然后系统读取JSON文件加载为全量索引）
* Binlog的实现监听，系统根据模板JSON解析Binlog,将event转化为BinlogRowData,
* 系统启动就通过BinlogClient，自行监听Binlog，将BinlogRowData转化为MysqlRowData
*
* [BinlogRowData和MysqlRowData,都是可以直接使用的数据。
* mysql版的才是用户所真正需要的RowData
* 但是为了保持元数据尽量少丢失，直接转化而来的Binlog版的RowData也留下了。
* 因此两个版本的RowData就多了一层转换。
* ]
*
* 获得了增量数据，进行增量数据的投递 -（投递方式1）
* 所谓增加数据的投递，即将MysqlRowData转化为table类型
* 最后调用levelDataHandle方法，
* 实现增量索引（增量数据加载为增量索引）
*
* 增量数据投递到Kafka -(投递方式2)
* why？
* 因为可能广告数据需要传给广告分析系统，而不只是在检索系统使用。
* RowData 通过JSON序列化后，投放进kafka,其他系统监听Kafka获取对应的数据。
* 类似于检索系统通过监听Binlog获取对应的数据。
*
* 检索服务和外界（媒体方）进行交互
*
* 三大限制条件的匹配方法
* 根据三大限制条件进行过滤
*
* 实现跨索引查询，类似于联表查询。
* 根据多个unitId，即unitIds，获取到多个创意对象，即creativeObjects
先获取unitObjects，
然后通过unitCreativeMap的get(),传入unitId，拿到creativeId，根据creativeIds获取到creativeObjects
*
*
*
*
* */

