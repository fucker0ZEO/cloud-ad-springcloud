package com.cs.ad.mysql.dto;

import com.cs.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {

    private String tableName;
    private String level;

    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     * 这个map表示的映射：
     * 字段索引 ->  字段名
     *
     * BinLog中不会表示出所操作的列的名字，
     * 只会表示出[0,1,2]这类的索引，
     * 需要根据具体的索引映射到字段名。
     * 因此需要这个positionMap实现映射。
     * 即字段索引作为key，字段名作为value
    * */
    private Map<Integer,String> positionMap = new HashMap<>();

}
