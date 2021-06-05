package com.cs.ad.index.adUnit;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fucker
 */
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long,AdUnitObject> {
    /**同样定义存储整个索引对象的并发map*/
    private static Map<Long,AdUnitObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
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
