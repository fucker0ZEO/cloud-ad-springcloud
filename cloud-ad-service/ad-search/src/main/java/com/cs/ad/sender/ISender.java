package com.cs.ad.sender;

import com.cs.ad.mysql.dto.MysqlRowData;

/**
 * @author fucker
 *
 * 投递增量数据的接口
 */
public interface ISender {

    /**sender 投递增量数据的实现方法
     */
    void sender(MysqlRowData rowData);
}
