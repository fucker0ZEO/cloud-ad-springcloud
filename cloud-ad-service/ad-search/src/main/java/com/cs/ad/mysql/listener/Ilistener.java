package com.cs.ad.mysql.listener;

import com.cs.ad.mysql.dto.BinlogRowData;

/**
 * @author fucker
 * 对增量索引实现更新的接口
 * 通过实现类，对Binlog实现不同的内容
 * 例如将binlog传递出去，例如实现Binlog的更新
 *
 * 监听Binlog，并把Binlog对应的event解析成
 * 自定义的BinlogRowData
 * 之后实现将binlogRowData传递出去，实现增量数据索引的更新
 */
public interface Ilistener {

    /**对不同的表实现不同的更新方法
     * 因此需要注册不同的监听器*/
    void register();

    /**event已经转化为了BinlogRowData
     * 可以实现增量数据的更新*/
    void onEvent(BinlogRowData eventData);
}
