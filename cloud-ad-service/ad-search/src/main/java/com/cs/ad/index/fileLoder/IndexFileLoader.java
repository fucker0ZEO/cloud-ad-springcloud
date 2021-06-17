package com.cs.ad.index.fileLoder;

import com.alibaba.fastjson.JSON;
import com.cs.ad.dump.DConstant;
import com.cs.ad.dump.table.*;
import com.cs.ad.handler.AdLevelDataHandler;
import com.cs.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fucker
 * 索引加载类
 * 根据数据表导出的文件，读取文件，加载出索引
 * 这个类是AdLevelDataHandler类的调用者
 *
 * IndexFileLoader依赖于DataTable
 * @DependsOn("dataTable") 这个注解定义出 依赖关系
 * DataTable如同目录存储索引
 *
 * init中加载索引需要保证顺序不变，从第2层级到第4层级
 * 层级之间存在依赖关系，第4层级依赖于第2层级。
 * 缺少低层级的数据就会导致失败
 */
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    /**系统启动时通过init()初始化全量索引
     * 传入的操作类型都是ADD，即执行加载索引*/
    @PostConstruct
    public void init() {
        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*加载adPlan表*/
         List<String> adPlanStrings = loadDumpData(
                 /*拼接字符串*/
                String.format("%s%s",
                        /*指定的目录*/
                        DConstant.DATA_ROOT_DIR,
                        /*需要读取的文件名*/
                        DConstant.AD_PLAN)
        );
         /*遍历adPlan字符串数据，并填充到到handleLevel2中，实现索引的加载*/
        adPlanStrings.forEach(p -> AdLevelDataHandler.handleLevel2(
                /*实现JSON反序列为Table对象。
                handleLevel2中所需的参数是Table对象而非JSON*/
                JSON.parseObject(p, AdPlanTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));


        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adCreative表*/
        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE)
        );
        /*遍历读取到的adCreative数据，并填充到到handleLevel2中，实现索引的加载*/
        adCreativeStrings.forEach(c -> AdLevelDataHandler.handleLevel2(
                /*实现JSON反序列化为Table对象
                *handleLevel2中所需的参数是Table对象而非JSON*/
                JSON.parseObject(c, AdCreativeTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));

         /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adUnit表*/
        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT)
        );
        /*遍历读取到的adUit字符串数据，并填充到到handleLevel3中，实现索引的加载*/
        adUnitStrings.forEach(u -> AdLevelDataHandler.handleLevel3(
                /*实现JSON反序列化为Table对象
                 *handleLevel3中所需的参数是Table对象而非JSON*/
                JSON.parseObject(u, AdUnitTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));

        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adCreativeUnit表*/
        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_CREATIVE_UNIT)
        );
        /*遍历读取到的adCreativeUnit字符串数据，并填充到到handleLevel3中，实现索引的加载*/
        adCreativeUnitStrings.forEach(cu -> AdLevelDataHandler.handleLevel3(
                /*实现JSON反序列化为Table对象
                 *handleLevel3中所需的参数是Table对象而非JSON*/
                JSON.parseObject(cu, AdCreativeUnitTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));

        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adUnitDistrict表*/
        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_DISTRICT)
        );
        /*遍历读取到的adUnitDistrict字符串数据，并填充到到handleLevel4中，实现索引的加载*/
        adUnitDistrictStrings.forEach(d -> AdLevelDataHandler.handleLevel4(
                /*实现JSON反序列化为Table对象
                 *handleLevel4中所需的参数是Table对象而非JSON*/
                JSON.parseObject(d, AdUnitDistrictTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));

        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adUnitIt表*/
        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_IT)
        );
        /*遍历读取到的adUnitIt字符串数据，并填充到到handleLevel4中，实现索引的加载*/
        adUnitItStrings.forEach(i -> AdLevelDataHandler.handleLevel4(
                /*实现JSON反序列化为Table对象
                 *handleLevel4中所需的参数是Table对象而非JSON*/
                JSON.parseObject(i, AdUnitItTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));

        /*调用读取本来中读取文件的方法，传入文件名，拿到文件对象
        依次加载每张表中的数据*/
        /*读取adUnitKeyword表*/
        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DIR,
                        DConstant.AD_UNIT_KEYWORD)
        );
        /*遍历读取到的 adUnitKeyword字符串数据，并填充到到handleLevel4中，实现索引的加载*/
        adUnitKeywordStrings.forEach(k -> AdLevelDataHandler.handleLevel4(
                /*实现JSON反序列化为Table对象
                 *handleLevel4中所需的参数是Table对象而非JSON*/
                JSON.parseObject(k, AdUnitKeywordTable.class),
                /*传入操作数ADD*/
                OpType.ADD
        ));


    }



    /**根据传入的文件名读取文件*/
    private List<String> loadDumpData(String fileName){
        try (BufferedReader bufferedReader = Files.newBufferedReader(
                /*传入文件名，返回对应的文件路径，new出对应的文件对象*/
                Paths.get(fileName)
        )){
            /*返回为 每一行都是List<String>*/
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
