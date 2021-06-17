package com.cs.ad.handler;

/*
* AdLevelDataHandler 为索引类的直接调用者
* new 实体类Object，并根据已经整理好字段的table对象填充实体类object
* 通过DataTable.of(xx.class),拿到索引对象，即并发map
* 有了实体类object，自然也就有了实体类对应的ID，例如planId
* Id为k,实体类object为value
*
* 定义handleBinlogEvent方法，
* 传入 索引对象-->IndexAware<K,V> index
* 传入 实体类Id-->K key
* 传入 实体类对象-->V value
* 传入 操作type-->OpType type
* 使用switch语句根据type，让索引对象执行不同的方法
* 例如index.ADD
* 它会根据不同的索引对象跳转到IndexAware接口对应的实现类，
* 例如传入planIndex，就会跳转到AdPlanIndex.java
* 然后调用其中的方法，方法的参数是传入的k,v，即实体类id和实体类object
*
* 参考handleLevel3的AdCreativeUnitTable作为示例
*
* */