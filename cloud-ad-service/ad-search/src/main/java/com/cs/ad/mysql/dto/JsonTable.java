package com.cs.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 表达出模板文件中的表
 * 属性和模板文件一一对应
 *
 * 模板JSON文件如下
 *
 *   "database": "ad_data",
 *   "tableList": [
 *     {
 *       "tableName": "ad_plan",
 *       "level": 2,
 *       "insert": [
 *         {"column": "id"},
 *         {"column": "user_id"},
 *         {"column": "plan_status"},
 *         {"column": "start_date"},
 *         {"column": "end_date"}
 *       ],
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonTable {

    /**表名和层级*/
    private String  tableName;
    private Integer level;

    /**模板文件中的3个对表的操作*/
    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Column {

        private String column;
    }
}
