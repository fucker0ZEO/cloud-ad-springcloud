package com.cs.ad.mysql.dto;

import com.cs.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 * MySQL的增量数据
 * 后续需要投递增量数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysqlRowData {

    /**表名，因为是单数据库，不需要库名*/
    private String tableName;
    /**层级关系*/
    private String level;
    /**自定义的操作数类型
     * 根据eventType转化为OpType*/
    private OpType opType;

    /**字段值映射,多个map组成的一个list
     * 单个map中的k为colName, v为colValue
     * k,v相关的数据参考AggregationListener类中的buildRowData*/
    private List<Map<String,String>> fieldValueMap =new ArrayList<>();

}
