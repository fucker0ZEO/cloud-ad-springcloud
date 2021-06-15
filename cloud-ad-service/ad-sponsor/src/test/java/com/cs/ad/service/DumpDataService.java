package com.cs.ad.service;

import com.alibaba.fastjson.JSON;
import com.cs.ad.ApplicationTest;
import com.cs.ad.constant.CommonStatus;
import com.cs.ad.dao.AdPlanRepository;
import com.cs.ad.dao.AdUnitRepository;
import com.cs.ad.dao.CreativeRepository;
import com.cs.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.cs.ad.dao.unit_condition.AdUnitItRepository;
import com.cs.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.cs.ad.dao.unit_condition.CreativeUnitRepository;
import com.cs.ad.dump.DConstant;
import com.cs.ad.dump.table.*;
import com.cs.ad.entity.AdPlan;
import com.cs.ad.entity.AdUnit;
import com.cs.ad.entity.Creative;
import com.cs.ad.entity.unit_condition.AdUnitDistrict;
import com.cs.ad.entity.unit_condition.AdUnitIt;
import com.cs.ad.entity.unit_condition.AdUnitKeyword;
import com.cs.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现导出服务
 * @RunWith(SpringRunner.class) 注解标记为测试用例
 *
 * 标记为springBoot的测试用例+指定启动类+标记为无web环境
 * @SpringBootTest(classes = {ApplicationTest.class},
 *         webEnvironment = SpringBootTest.WebEnvironment.NONE)
 *
 * */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {

    /*注入所有数据表的DAO接口*/
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository districtRepository;
    @Autowired
    private AdUnitItRepository itRepository;
    @Autowired
    private AdUnitKeywordRepository keywordRepository;


    /*测试用例
    * 调用导出方法实现导出*/
    @Test
    public void dumpAdTableData() {

        dumpAdPlanTable(
                /*传入常量类中的文件目录和文件名*/
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_PLAN)
        );
        dumpAdUnitTable(
                /*传入常量类中的文件目录和文件名*/
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT)
        );
        dumpAdCreativeTable(
                /*传入常量类中的文件目录和文件名*/
                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE)
        );
        dumpAdCreativeUnitTable(
                /*传入常量类中的文件目录和文件名*/

                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE_UNIT)
        );
        dumpAdUnitDistrictTable(
                /*传入常量类中的文件目录和文件名*/

                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_DISTRICT)
        );
        dumpAdUnitItTable(
                /*传入常量类中的文件目录和文件名*/

                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_IT)
        );
        dumpAdUnitKeywordTable(
                /*传入常量类中的文件目录和文件名*/

                String.format("%s%s", DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_KEYWORD)
        );
    }

    /*将adPlan表字段导出为JSON并通过Buffer写入指定的文件中*/

    private void dumpAdPlanTable(String fileName) {

        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(
                /*导出有效状态的数据*/
                CommonStatus.VALID.getStatus()
        );
        /*获取的记录如果为null则return 空*/
        if (CollectionUtils.isEmpty(adPlans)) {
            return;
        }

        /*new planTable对象，用来存储需要导出的数据*/
        List<AdPlanTable> planTables = new ArrayList<>();
        /*for循环遍历记录，将查询出的记录中的字段填充到Tables对象中*/
        adPlans.forEach(p -> planTables.add(
                new AdPlanTable(
                        p.getId(),
                        p.getUserId(),
                        p.getPlanStatus(),
                        p.getStartDate(),
                        p.getEndDate()
                )
        ));

        /*通过paths的get方法获取文件路径*/
        Path path = Paths.get(fileName);
        /*根据文件路径获取到对象的文件对象，操作文件对象就相当于操作该文件*/
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            /*遍历tables，并转化为JSON数据，写入文件中*/
            for (AdPlanTable planTable : planTables) {
                writer.write(JSON.toJSONString(planTable));
                /*写一个新行，即每次写入都会换行*/
                writer.newLine();
            }
            /*关闭IO流*/
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdPlanTable error");
        }
    }

    /*将adUnit表字段导出为JSON并通过Buffer写入指定的文件中
    * 具体步骤同adPlan,剩下的几个表也是类似的*/
    private void dumpAdUnitTable(String fileName) {

        List<AdUnit> adUnits = unitRepository.findAllByUnitStatus(
                /*导出有效状态的数据*/
                CommonStatus.VALID.getStatus()
        );
        /*获取的记录如果为null则return 空*/
        if (CollectionUtils.isEmpty(adUnits)) {
            return;
        }
        /*new planTable对象，用来存储需要导出的数据*/

        List<AdUnitTable> unitTables = new ArrayList<>();
        /*for循环遍历记录，将查询出的记录中的字段填充到Tables对象中*/
        adUnits.forEach(u -> unitTables.add(
                new AdUnitTable(
                        u.getId(),
                        u.getUnitStatus(),
                        u.getPositionType(),
                        u.getPlanId()
                )
        ));
        /*通过paths的get方法获取文件路径*/

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitTable unitTable : unitTables) {
                /*遍历tables，并转化为JSON数据，写入文件中*/

                writer.write(JSON.toJSONString(unitTable));
                /*写一个新行，即每次写入都会换行*/
                writer.newLine();
            }
            /*关闭IO流*/

            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdUnitTable error");
        }
    }

    private void dumpAdCreativeTable(String fileName) {

        List<Creative> creatives = creativeRepository.findAll();
        if (CollectionUtils.isEmpty(creatives)) {
            return;
        }

        List<AdCreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(c -> creativeTables.add(
                new AdCreativeTable(
                        c.getId(),
                        c.getName(),
                        c.getType(),
                        c.getMaterialType(),
                        c.getHeight(),
                        c.getWidth(),
                        c.getAuditStatus(),
                        c.getUrl()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeTable creativeTable : creativeTables) {
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdCreativeTable error");
        }
    }

    private void dumpAdCreativeUnitTable(String fileName) {

        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if (CollectionUtils.isEmpty(creativeUnits)) {
            return;
        }

        List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(c -> creativeUnitTables.add(
                new AdCreativeUnitTable(
                        c.getCreativeId(),
                        c.getUnitId()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeUnitTable creativeUnitTable : creativeUnitTables) {
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdCreativeUnit error");
        }
    }

    private void dumpAdUnitDistrictTable(String fileName) {

        List<AdUnitDistrict> unitDistricts = districtRepository.findAll();
        if (CollectionUtils.isEmpty(unitDistricts)) {
            return;
        }

        List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();
        unitDistricts.forEach(d -> unitDistrictTables.add(
                new AdUnitDistrictTable(
                        d.getUnitId(),
                        d.getProvince(),
                        d.getCity()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitDistrictTable unitDistrictTable : unitDistrictTables) {
                writer.write(JSON.toJSONString(unitDistrictTable));
                writer.newLine();
            }
//            /*关闭IO流*/
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdUnitDistrictTable error");
        }
    }

    private void dumpAdUnitItTable(String fileName) {

        List<AdUnitIt> unitIts = itRepository.findAll();
        if (CollectionUtils.isEmpty(unitIts)) {
            return;
        }

        List<AdUnitItTable> unitItTables = new ArrayList<>();
        unitIts.forEach(i -> unitItTables.add(
                new AdUnitItTable(
                        i.getUnitId(),
                        i.getItTag()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitItTable unitItTable : unitItTables) {
                writer.write(JSON.toJSONString(unitItTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdUnitItTable error");
        }
    }

    private void dumpAdUnitKeywordTable(String fileName) {

        List<AdUnitKeyword> unitKeywords = keywordRepository.findAll();
        if (CollectionUtils.isEmpty(unitKeywords)) {
            return;
        }

        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(k -> unitKeywordTables.add(
                new AdUnitKeywordTable(
                        k.getUnitId(),
                        k.getKeyWord()
                )
        ));

        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitKeywordTable unitKeywordTable : unitKeywordTables) {
                writer.write(JSON.toJSONString(unitKeywordTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            log.error("dumpAdUnitItTable error");
        }
    }
}
