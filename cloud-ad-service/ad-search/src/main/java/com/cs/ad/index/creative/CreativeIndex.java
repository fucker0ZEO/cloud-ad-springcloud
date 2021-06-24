package com.cs.ad.index.creative;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fucker
 * IndexAware<Long,CreativeObject>
 *     这里的Long是指主键为Long类型
 *     根据主键查询，得到CreativeObject
 */
@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long,CreativeObject> {

    /**只有正向索引*/
    private static Map<Long,CreativeObject> objectMap;
    static {
        /*使用并发map来初始化*/
        objectMap = new ConcurrentHashMap<>();
    }


    /**根据多个创意ID，获取到多个创意对象*/
    public List<CreativeObject> fetch(Collection<Long> adIds){
//        传参校验。每次传参都需要校验
        if (CollectionUtils.isEmpty(adIds)){
//            返回空列表
            return Collections.emptyList();
        }
//        否则创建result作为返回对象
        List<CreativeObject> result = new ArrayList<>();
//        遍历创意Id集合，拿到每个创意id,
//        然后调用本类的get方法获取到每个id对应的object，
//        最后将object存入result返回
        adIds.forEach(u -> {
            CreativeObject object = get(u);
//            如果获取的创意对象不存在，打印错误日志
            if (null == object){
                log.error("CreativeObject mot found: {}",u);
            }
            result.add(object);
        });
//        返回result
        return result;
    }


    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject value) {

        objectMap.put(key, value);

    }

    @Override
    public void update(Long key, CreativeObject value) {
        /*写入数据之前先校验,写入的value值是否为null*/
        if(value == null){
            log.error("");
        }
        /*再校验，该key在索引中是否已存在*/
        CreativeObject oldObject = objectMap.get(key);
        if (null == oldObject){
            /*如果不存在这行记录，可以直接put,增加一行记录*/
            objectMap.put(key, value);
        }else{
            /*所有校验都完成可以调用自定义的update方法，依次更新值*/
            oldObject.update(value);
        }

    }

    @Override
    public void delete(Long key, CreativeObject value) {
        objectMap.remove(key);
    }
}
