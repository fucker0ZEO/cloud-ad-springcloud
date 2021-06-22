package com.cs.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.cs.ad.dump.table.*;
import com.cs.ad.handler.AdLevelDataHandler;
import com.cs.ad.index.DateLevel;
import com.cs.ad.mysql.constant.Constant;
import com.cs.ad.mysql.dto.MysqlRowData;
import com.cs.ad.sender.ISender;
import com.cs.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 *
 *  @Component("indexSender") bean命名为indexSender
 * 而 @Resource(name = "indexSender") 即可获取到这个对应名称的bean
 *
 * 增加数据的投递，即将MysqlRowData转化为table类型
 */
@Slf4j
@Component("indexSender")
public class IndexSender implements ISender {

    /**根据不同的数据层级，完成投递*/
    @Override
    public void sender(MysqlRowData rowData) {

        String level = rowData.getLevel();
//        如果传入的rowData都level为第2层级
        if (DateLevel.LEVEL2.getLevel().equals(level)){
//            调用leve2的方法实现数据填充，并调用handleLevel2()，加载增量索引
            Level2RowData(rowData);
            // 如果传入的rowData都level为第3层级
        }else if (DateLevel.LEVEL3.getLevel().equals(level)){

            //如果传入的rowData都level为第4层级
        }else if (DateLevel.LEVEL4.getLevel().equals(level)){

//            如果都不是则打印异常日志
        }else {
            log.error("MysqlRowData ERROR: {}", JSON.toJSONString(rowData));

        }
    }


    /**    第2层级的增量数据的投递,将MysqlRowData填充为table数据
     */
    private void Level2RowData(MysqlRowData rowData) {

        //校验rowData中的table的名字是constant中定义的adPlan
        if (rowData.getTableName().equals(
                Constant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            //定义planTables，用来存储填充值后的planTable
            List<AdPlanTable> planTables = new ArrayList<>();

            /*遍历FieldValueMap()，填充入fieldValueMap中*/
            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {

                /*new 一个AdPlanTable对象*/
                AdPlanTable planTable = new AdPlanTable();

//                根据Map中的key，循环将v填充入planTable的各个属性中
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(
                                    CommonUtils.parseStringDate(v)

                            );
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(
                                    CommonUtils.parseStringDate(v)
                            );
                            break;
                    }
                });
                /*将planTable对象放入planTables列表中*/
                planTables.add(planTable);
            }
            /*根据OpType操作类型，调用handleLevel2加载增量索引*/
            planTables.forEach(p ->
                    AdLevelDataHandler.handleLevel2(p, rowData.getOpType()));

            //校验rowData中的table的名字是constant中定义的adCreative
        } else if (rowData.getTableName().equals(
                Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME
        )) {
//
            List<AdCreativeTable> creativeTables = new ArrayList<>();

            for (Map<String, String> fieldValeMap :
                    /*.getFieldValueMap()*/
                    rowData.getFieldValueMap()) {

                AdCreativeTable creativeTable = new AdCreativeTable();

                fieldValeMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(v);
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            }
            creativeTables.forEach(c ->
                    AdLevelDataHandler.handleLevel2(c, rowData.getOpType()));
        }
    }

    /**    第3层级的增量数据的投递,将MysqlRowData填充为table数据
     */
    private void Level3RowData(MysqlRowData rowData) {
        /*校验tableName是否为adUnit*/
        if (rowData.getTableName().equals(
                Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {
            /*new unitTables用来存unitTable*/
            List<AdUnitTable> unitTables = new ArrayList<>();

            /*遍历FieldValueMap()
            * FieldValueMap()是一个map列表
            * 通过遍历得到单个map
            * */
            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {

                AdUnitTable unitTable = new AdUnitTable();

                /*对单个map操作，
                单个map中的k为colName，即字段名，
                v为colValue，即属性值。诸多字段名 -> 属性值 组成了整个map列表
                根据map的k，将v填充到unitTable的各个属性中*/
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                    }
                });
                /*将填充值后的table填充到tables列表中*/
                unitTables.add(unitTable);
            }
            /*根据tables的数据，加载增量索引*/
            unitTables.forEach(u ->
                    /*调用handleLevel3()，根据传入的操作类型和table数据变更索引*/
                    AdLevelDataHandler.handleLevel3(u, rowData.getOpType()));


            /*第3层级，不止有unitTable，还有creativeUnitTable
            * 校验table名是否为creativeUnitTable*/
        } else if (rowData.getTableName().equals(
                Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME
        )) {
            /*new tables用来存table*/
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();

            /*遍历字段 -> 属性值 映射列表
            * 得到单独的map*/
            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {

                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();

                /*将单个map中的属性值，根据字段名填充到new的table对象中
                * 此时相当于是字段名已经固定，不再需要维护一张映射关系表/map，填充值即可
                * */
                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });
                /*将table填充到tables中*/
                creativeUnitTables.add(creativeUnitTable);
            }
            /*根据tables的数据，加载增量索引*/
            creativeUnitTables.forEach(
                    /*调用handleLevel3()方法，
                    根据操作类型和tables数据对现存的索引进行变更
                    即为加载增量索引*/
                    u -> AdLevelDataHandler.handleLevel3(u, rowData.getOpType())
            );
        }
    }

    /**    第4层级的增量数据的投递,将MysqlRowData填充为table数据
     * 操作类似于前面2个，可参考前面的，不再注释赘述
     */
    private void Level4RowData(MysqlRowData rowData) {
        /*第3层级是3个限制维度，因此使用switch判断
        * switch -> for -> switch
        * 3层嵌套，略多了*/
        switch (rowData.getTableName()) {

            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {

                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });

                    districtTables.add(districtTable);
                }

                districtTables.forEach(
                        d -> AdLevelDataHandler.handleLevel4(d, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                List<AdUnitItTable> itTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {

                    AdUnitItTable itTable = new AdUnitItTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                itTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                itTable.setItTag(v);
                                break;
                        }
                    });
                    itTables.add(itTable);
                }
                itTables.forEach(
                        i -> AdLevelDataHandler.handleLevel4(i, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:

                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                        }
                    });
                    keywordTables.add(keywordTable);
                }

                keywordTables.forEach(
                        k -> AdLevelDataHandler.handleLevel4(k, rowData.getOpType())
                );
                break;
        }
    }


}
