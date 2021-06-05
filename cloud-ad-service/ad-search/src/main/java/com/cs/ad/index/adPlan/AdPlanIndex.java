package com.cs.ad.index.adPlan;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fucker
 * @Component注解作为spring组件
 * Long,对应接口中的k，一般都是通过主键id来索引，因此为long
 * 类型
 * AdPlanObject，索引中数据记录的实体类对象。根据k拿到v，根据主键id
 * 拿到对应的数据记录，完成一 一 对应
 */
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {

    /**定义存放索引的数据结构--map
     * map本质还是hash，这里算是hash索引
     * objectMap就代表索引对象
     * 索引存在并发读写，因此使用ConcurrentHashMap
     * */
    private static Map<Long, AdPlanObject> objectMap;

    /*初始化索引*/
    static{
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        /*索引Map的get方法实现*/
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add: {}", objectMap);
        /*索引Map的put方法实现*/
        objectMap.put(key,value);
        log.info("after add: {}", objectMap);

    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update: {}", objectMap);
        /*根据key查询索引
        来校验对应的数据记录是否存在，
        存在就更新，不存在就增加
        oldObject 代表一行索引中具体记录对应的实体类
        而objectMap代表整个索引对象
        */
        AdPlanObject oldObject = objectMap.get(key);
        if (oldObject == null){
            objectMap.put(key, value);
        }else{
            /*数据记录实体类中
            自定义的update方法实现数据单值的依次更新*/
            oldObject.update(value);
        }
        log.info("after update: {}", objectMap);

    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", objectMap);
        /*索引Map的delete方法实现*/
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }
}
