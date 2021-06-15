package com.cs.ad.handler;

import com.alibaba.fastjson.JSON;
import com.cs.ad.dump.table.AdCreativeTable;
import com.cs.ad.dump.table.AdCreativeUnitTable;
import com.cs.ad.dump.table.AdPlanTable;
import com.cs.ad.dump.table.AdUnitTable;
import com.cs.ad.index.DataTable;
import com.cs.ad.index.IndexAware;
import com.cs.ad.index.adPlan.AdPlanIndex;
import com.cs.ad.index.adPlan.AdPlanObject;
import com.cs.ad.index.adUnit.AdUnitIndex;
import com.cs.ad.index.adUnit.AdUnitObject;
import com.cs.ad.index.creative.CreativeIndex;
import com.cs.ad.index.creative.CreativeObject;
import com.cs.ad.mysql.constant.OpType;
import lombok.extern.slf4j.Slf4j;

/**定义通用方法实现全量/增量索引的更新过程
 * @author fucker
 * 1.索引之间存在着层级的划分，也就是依赖关系的划分
 * 因为不需要用户层级，最高层级变为了推广计划，也就是level2
 * 2.加载全量索引其实是增量索引“添加”的一种特殊实现，
 * 一次性添加大量的索引数据就相当于是全量索引
 * 这样实现代码时，可以实现全量和增量的特殊更新
 *
 * 第2层级：不依赖于其他索引的层级。此处是推广计划和创意作为第2层级
 * 第3层级：依赖于第2层级
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
                拿到对应的PlanIndex对象*/
                AdPlanIndex.class
                /*unitTable中包含有推广计划的ID
                * 使用get方法，根据PlanId获取PlanObject
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
