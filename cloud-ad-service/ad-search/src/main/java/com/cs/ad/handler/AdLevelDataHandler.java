package com.cs.ad.handler;

import com.alibaba.fastjson.JSON;
import com.cs.ad.dump.table.*;
import com.cs.ad.index.DataTable;
import com.cs.ad.index.IndexAware;
import com.cs.ad.index.Inverted.creativeUnit.CreativeUnitIndex;
import com.cs.ad.index.Inverted.creativeUnit.CreativeUnitObject;
import com.cs.ad.index.Inverted.district.UnitDistrictIndex;
import com.cs.ad.index.Inverted.interest.UnitItIndex;
import com.cs.ad.index.Inverted.keyword.UnitKeywordIndex;
import com.cs.ad.index.adPlan.AdPlanIndex;
import com.cs.ad.index.adPlan.AdPlanObject;
import com.cs.ad.index.adUnit.AdUnitIndex;
import com.cs.ad.index.adUnit.AdUnitObject;
import com.cs.ad.index.creative.CreativeIndex;
import com.cs.ad.index.creative.CreativeObject;
import com.cs.ad.utils.CommonUtils;
import com.cs.ad.mysql.constant.OpType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**定义通用方法实现全量/增量索引的更新过程
 * @author fucker
 * 1.索引之间存在着层级的划分，也就是依赖关系的划分
 * 因为不需要用户层级，最高层级变为了推广计划，也就是level2
 * 2.加载全量索引其实是增量索引“添加”的一种特殊实现，
 * 一次性添加大量的索引数据就相当于是全量索引
 * 这样实现代码时，可以实现全量和增量的特殊更新
 *
 * 第2层级：不依赖于其他索引的层级。此处是推广计划和创意作为第2层级
 * 第3层级：依赖于第2层级，此处是AdUnit和AdCreativeUnit。
 * unit依赖于plan，unit中含有planId和planobject
 * CreativeUnit依赖于unitId和creativeId
 * 第4层级：依赖于第3层级，推广单元维度的限制。依赖于推广单元。
 * 此处是unitDistrict和unitKeyword和unitIt
 *
 *
 * #############################################################
 * index对象：索引对象，也就是那些并发map
 * Object对象：索引中的记录对象，相当于数据记录所对应的实体类
 * table对象：导出的表数据中字段定义。最开始查询数据库出来的数据，
 * 不完全是所需要的数据。将DB中的数据重新填充/存储后的实体类对象就是table对象。
 * 最后也是从table对象转化为JSON写入到文件中
 *
 *
 * 现在需要读取JSON文件，变成一个完整的索引，自然也就需要table对象
 *
 * ##############################################################
 * 为何要用table对象填充新new的Object对象？
 * 因为最初的数据来自于DB中查询到的记录，但是DB中的记录不等于实体类Object
 * 而table对象是最后写入数据文件的对象，其中的字段经过了重新整理填充。
 * 因此这里可以直接拿来用。table中的数据刚好等同于实体类object中的数据
 *##############################################################
 * handleBinlogEvent的作用是什么？
 * 处理Binlog事件，其中会调用index.ADD等方法
 * [索引类的调用者，调用index实现类的方向]
 *
 * 传入对应的index对象，参数k为对象id，参数v为实体类object，
 * 然后调用对应实现类的索引Map的RUD方法
 *
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable,OpType type){
       /*将AdPlanTable中的数据填充到AdPlanObject中*/
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        /*调用本类中定义的静态handleBinlogEvent方法，传入对应的参数*/
        handleBinlogEvent(
                /*传入AdPlanIndex这个索引对象的类型获取到对应的IndexAware*/
                DataTable.of(AdPlanIndex.class),
                /*planObject中的Id作为key传入。
                实际上此处的planObject来自于planTable中的数据填充*/
                planObject.getPlanId(),
                /*planObject对应作为value，根据id拿到planObject*/
                planObject,
                /*常量中定义的操作类型*/
                type
        );
    }
    public static void handleLevel2(AdCreativeTable creativeTable,OpType type){
        /*构造creativeObject，
        将creativeTable中的数据填充到new出的CreativeObject中*/
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        /*调用本类中定义的静态handleBinlogEvent方法，传入对应的参数*/
        handleBinlogEvent(
                /*传入CreativeIndex这个索引对象的类型获取到对应的IndexAware*/
                DataTable.of(CreativeIndex.class),
                /*creativeObject中的Id作为key传入。
                实际上此处的CreativeObject来自于planTable中的数据填充*/
                creativeObject.getAdId(),
                /*creativeObject对应作为value，根据id拿到creativeObject*/
                creativeObject,
                /*常量中定义的操作类型*/
                type
        );

    }

    /**第3层级依赖于第2层级*/
    public static void handleLevel3(AdUnitTable unitTable,OpType type){
        AdPlanObject adPlanObject = DataTable.of(
                /*传入PlanIndex的类型，
                拿到对应的PlanIndex对象，即并发map索引*/
                AdPlanIndex.class
                /*unitTable中包含有推广计划的ID
                * 使用get方法，返回值为objectMap.get(key)
                * 根据PlanId这个key，获取PlanObject这个value
                * */
        ).get(unitTable.getPlanId());
        /*[索引之间存在依赖关系，第3层级的索引依赖于第2层级的索引]
        AdUnitTable依赖于adPlan，没有AdPlanId,那unitTable就不完整
        unit表中有对应的Plan相关的数据，索引中也要PlanObject
        看着像嵌套，类似于DDD中的model聚合

        如果获取不到则代表不应该被加载到当前的索引中
        * 不能根据unitTable中的planId获取planObject
        第3层级unit，对第2层级Plan做校验
        */
        if (null == adPlanObject){
            /*打印错误日志并返回*/
            log.error("handleLevel3 found AdPlanObject error: {}",
                    unitTable.getPlanId());
           return;
        }

        /*否则可以根据Plan来new AdUnitObject对象*/
        AdUnitObject unitObject = new AdUnitObject(
                /*填充数据，需要保证数据的顺序一致*/
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                /*前面校验过adPlanObject不为null，
                因此直接传入即可*/
                adPlanObject
        );
        handleBinlogEvent(
                /*通过DataTable注入Index*/
                DataTable.of(AdUnitIndex.class),
                /*unitId为key*/
                unitTable.getUnitId(),
                /*unitObject为value*/
                unitObject,
                /*操作类型*/
                type
        );

    }

    /**另一个第3层级
     * creative和Unit两个第2层级的数据，关联的索引
     * 最常见的关系往往是11对应，但在多对多中，一个ID在对应着多行记录。
     * 例如某个ID的用户，在某个时间段的购物订单。
     * 这个时间段内就会有多张订单指向同一个用户
     *
     * 因此需要有一张关联表
     * */
    public static void handleLevel3(
            AdCreativeUnitTable creativeUnitTable, OpType type){

        /*倒排索引的实现中没有支持update操作，因此如果操作类型是更新类型，则直接返回*/
        if (type == OpType.UPDATE){
            log.error("it index can not support update");
            return;
        }

        /*校验unitObject和planObject是否存在
        * 在单独的关联表中，两大关联主体是核心，这2个字段对应的数据必须存在*/

        AdUnitObject unitObject = DataTable.of(
                /*根据UnitId获取unitObject对象*/
                AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        /*传入CreativeIndex的类型，拿到CreativeIndex
        * 根据它的get方法，传入创意Id，获取CreativeObject*/
        CreativeObject creativeObject = DataTable.of(
                CreativeIndex.class
                /*AdId，即创意ID*/
                ).get(creativeUnitTable.getAdId());
        /*如果推广单元或者是创意，
        有一个为null，则不应该建立该索引*/
        if (null == unitObject || null == creativeObject){
            /*打印一行异常日志*/
            log.error("AdCreativeUnitTable index error: {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }
        /*根据creativeUnitTable填充new的实体类对象，
        便于后面调用索引实现类中的方法时直接传入实体类对象作为value*/
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                /*调用连字符拼接方法，传入创意Id和推广单元Id完成拼接*/
                CommonUtils.stringConcat(
                        creativeUnitTable.getAdId().toString(),
                        creativeUnitTable.getUnitId().toString()
                ),
                /*传入creativeUnitObject作为value*/
                creativeUnitObject,
                /*最后传入操作类型*/
                type

        );
    }


    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable,OpType type){

        /*不支持update操作，因此需要校验*/
        if (type == OpType.UPDATE){
            log.error("district index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                /*根据class，获得unitIndex对象
                * 即并发Map*/
                AdUnitIndex.class
                /*根据unitId这个key获取到unitObject这个实体类对象*/
        ).get(unitDistrictTable.getUnitId());

        /*校验unitObject是否存在*/
        if (unitObject == null){
            log.error("AdUnitDistrictTable index error: {}",
                    unitDistrictTable.getUnitId());
            return;
        }

        /*key是2个值的连字符拼接而成*/
        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );

        /*value和singleton???
        * 将传入的1个ID变成set
        * 例如 [1]
        * 填充为value*/
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );

        /*调用索引的方法*/
        handleBinlogEvent(
                /*传入index索引对象*/
                DataTable.of(UnitDistrictIndex.class),
                /*传入key和value
                * 拼接成的字符串作为key
                * set类型的id为value*/
                key, value,
                /*传入操作类型type*/
                type
        );
    }


    /**类似地域维度*/
    public static void handleLevel4(AdUnitItTable unitItTable,OpType type){

        /*校验是否为update操作*/
        if (type == OpType.UPDATE){
            log.error("it index can not support update");
            return;
        }

        /*根据class类型
        * 拿到索引对象
        * 根据unitId查询索引对象
        * 拿到其中的unitObject*/
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitItTable.getUnitId());

        /*校验unitObject是否存在*/
        if (unitObject == null){
            log.error("AdUnitItTable index error: {}",
                    unitItTable.getUnitId());
            return;
        }

        /*value和singleton???
         * 将传入的1个ID变成set
         * 例如 [1]
         * 填充为value*/
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                /*ItTag为key*/
                unitItTable.getItTag(),
                /*set类型的unitId为value*/
                value,
                type
        );

    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable,
                                    OpType type) {

        if (type == OpType.UPDATE) {
            /*没有日志非常难以定位错误
            * 需要有写日志的习惯*/
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    keywordTable.getUnitId());
            return;
        }

        /*value和singleton???
         * 将传入的1个ID变成set
         * 例如 [1]
         * 填充为value*/
        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                /*key为关键词*/
                keywordTable.getKeyword(),
                value,
                type
        );
    }


    /*定义函数实现对索引的RUD的过程
    * 因为全量和增量不区分，这里可以一次性实现*/

    private static <K,V> void handleBinlogEvent(
            /*index包下的接口，实现对索引的CRUD操作*/
            IndexAware<K,V> index,
            /*传入的K,V*/
            K key,
            V value,
            /*mysql.constant包下定义的OpType的枚举类
            * ADD,UPDATE都是枚举类中定义的操作type*/
            OpType type
    ){
        /*根据操作类型实现索引的各类操作*/
        switch(type){
            case ADD:
                index.add(key,value);
                /*每一个case都需要break掉，否则会不断向下执行*/
                break;
            case UPDATE:
                index.update(key, value);
                /*每一个case都需要break掉，否则会不断向下执行*/
                break;
            case DELETE:
                index.delete(key, value);
                /*每一个case都需要break掉，否则会不断向下执行*/
                break;
            /*默认声明都不执行*/
            default:
                break;

        }
    }
}
