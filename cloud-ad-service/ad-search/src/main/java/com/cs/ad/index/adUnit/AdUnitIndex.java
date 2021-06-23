package com.cs.ad.index.adUnit;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fucker
 * objectMap k为unitId,v为索引记录对象
 */
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long,AdUnitObject> {
    /**同样定义存储整个索引对象的并发map*/
    private static Map<Long,AdUnitObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }

    /**返回unitId的set集合
     * 媒体方需要的是unitId的集合，而不是整个索引对象。
     * 类似于SQL中返回整张表的主键
     * */
    public Set<Long> match(Integer positionType){

        Set<Long> adUnitIds = new HashSet<>();
        /*遍历索引map，k为unitId,v为索引记录对象*/
        objectMap.forEach((k,v) ->{
            /*校验adUnitObject中的流量类型,
            流量类型在其他层面都没有经过校验。
            但流量类型对推广单元是必须的数据*/
            if (AdUnitObject.isAdSlotTypeOK
                    (positionType,v.getPositionType())){
                /*将unitId存入adUnitIds*/
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }

    /**根据IDS获取到索引对象
     * 类似于SQL中的根据多个主键查询出多行记录*/
    public List<AdUnitObject> fetch(Collection<Long> adUnitIds){

        /*校验传入的ids是否为null*/
        if (CollectionUtils.isEmpty(adUnitIds)){
            /*返回空集合*/
            return Collections.emptyList();
        }

        /*构造返回对象，即记录对象列表*/
        List<AdUnitObject> result = new ArrayList<>();

        /*遍历ids，得到unitId*/
        adUnitIds.forEach(u -> {
            /*根据id查询索引对象记录
            * 本类中定义的get方法，根据id查询索引记录对象*/
            AdUnitObject Object = get(u);
            /*校验是否获取到了索引记录对象*/
            if (Object == null){
                /*获取失败，打印错误日志*/
                log.error("AdUnitObject not found: {}",u);
                return;
            }
            /*索引对象记录填充到 记录列表中*/
            result.add(Object);
        });
        /*返回记录列表*/
        return result;
    }



    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        objectMap.put(key, value);

    }

    @Override
    public void update(Long key, AdUnitObject value) {

        /*根据key，判断索引中是否存在该行数据记录
        * 存在则update，不存在则put*/
        AdUnitObject oldObject = objectMap.get(key);
        if (objectMap == null){
            objectMap.put(key, value);
        }else {
            oldObject.update(value);
        }
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        objectMap.remove(key);
    }
}
