package com.cs.ad.index.Inverted;

/*Inverted index 为倒排索引
这个包用Inverted命名
子包都会涉及到推广单元的限制维度&倒排索引
推广单元与限制维度的值之间的关系是1对多
限制维度的值就像是检索的tags，例如关键词维度下就多个关键词，地域维度下有多个地域
1个id，多个关键词，多个地域维度，多个兴趣tag

它们的CRUD逻辑与单纯的推广单元，推广计划之间的CRUD差别较大
其中流程为
1.创建正向，倒排2条索引。需要创建索引对象为并发map
2.CRUD
    查询：倒排索引get查
    增加/删除：{需要实现部分增删，即对单个关键词等限制维度单值的增加}

    最关键的操作在CommonUtils工具类，具体解释如下：
       根据String key和倒排索引map对象，new的并发跳表set传入工具类
       完成对索引map插入 <key,set>,new的set默认的value为null。
       因为插入的set，传给map的是引用，不是固定的数值
       因此后续set的增删，都可以同步到影响整个索引map
       * 整理一下：
       * 传入key，索引map，并发跳表set
       * 校验key在map中是否存在
       * 不存在则将 <key,set>插入map，此处set=[]，即null
       * 最后返回map的value，即set

    倒排索引和正向索引的区别，在于传入工具类的key的区别，
    倒排索引和add/delete方法一致，传入的是String key，即索引的TagS
    正向索引则是直接传入遍历后的unitId，以推广单元的ID为key来查询并增删TagS。
        正向索引添加进入的本质上是增删<unitId,set<String>>，看上去string和unitId差不多
        但这里的set萎缩成了单值string
        1个id对应于一个String，但是这里大量String是重复的,String应该复用，id则集中放入set
        最优的方式是则是<a,[1,2,3,4]>,这也就是倒排索引的形式！！！
        最后的结果类似于：
                     <1,[a]>
                     <2,[a]>
                     <3,[a]>
                     <4,[a]>
        没有发挥出set的效果


    倒排索引：map:{阿里=[1, 2, 3, 4]}
    正向索引：map:{1=[阿里], 2=[阿里], 3=[阿里], 4=[阿里]}

 * */