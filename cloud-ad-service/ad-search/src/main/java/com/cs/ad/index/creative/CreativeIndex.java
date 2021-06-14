package com.cs.ad.index.creative;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
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
