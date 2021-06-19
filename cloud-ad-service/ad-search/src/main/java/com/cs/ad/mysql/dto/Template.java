package com.cs.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author fucker
 *
 * 表达数据库，只支持对1个数据库进行表达
 * 不过多数据库存储广告数据非常少见，一般也是一个数据库进行存储
 *
 * 对应着模板JSON文件中的
 *
 *   "database": "ad_data",
 *   "tableList": [
 *     {
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    private String database;
    private List<JsonTable> tableList;

}
