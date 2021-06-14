package com.cs.ad.index.Inverted.creativeUnit;

import com.cs.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author fucker
 * 本类中所有的adId都是指creativeId，即创意Id
 *倒排索引确定key后，不断在后面增加value即可，如：
 * <阿里,[1,2,3.....]>
 */
@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject> {
    /*构建3个索引*/

    /**<adId-unitId,creativeObject>*/
    private static Map<String, CreativeUnitObject> objectMap;

    /*<adId,Set<unitId>>
    * 创意与推广单元是多对多的关系
    * 根据创意Id，索引到对应的推广单元Id
    * */
    private static Map<Long, Set<Long>> creativeUnitMap;

    /**<unit,Set<adId>>
     * 和上面的反向
     * 根据推广单元id，索引到对应的创意Id
     * */
    private static Map<Long, Set<Long>> unitCreativeMap;

    /*并发map，初始化3条索引*/
    static{
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }


    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        /*依次更新三条索引*/
        objectMap.put(key, value);

        /*先拿到实体类，再根据实体类get，拿到其中的属性AdId,即创意Id
        * 根据创意Id查询索引，得到Set<unitId>*/
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());

        /*判断Set<unitId>是否为空集合，
        即判断{getAdId,Set<unitId>}是否存在
        * 不存在则new一个并发跳表Set并赋值给它
        * 由普通的set变成了并发跳表Set
        * 然后将更改put进索引*/
        if (CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAdId(),unitSet);
        }
        /*非空集合则直接添加传入的实体类中的unitId
        *类似于 <1,[9,8,7...]>,k和V是1对多，可以不断增加v的值
        *
        * 这里的逻辑存在一定问题，
        * 如果最初为<1,[9,8,7...]>那后续的[]就不是并发跳表
        * */
        unitSet.add(value.getUnitId());

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());

    }

    @Override
    public void update(String key, CreativeUnitObject value) {

        /*更新成本比较高，不提供更新接口*/
        log.error("");

    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        /*正向索引直接remove key完成删除*/
        objectMap.remove(key);

        /*<创意id,unitId>的倒排索引完成删除
        * 先尝试根据传入的CreativeUnitObject，
        * 获取到创意id，然后创意id作为key
        * 获取到对应的unitId集合
        * 创意id和unitId是一对多，如<1,[2,3,4,5,6]>
        * 可以对[]中的单值进行增删*/
        Set<Long> unitSet = creativeUnitMap.get(value.getAdId());

        /*如果unitSet不为空，
        即传入的CreativeUnitObject的unitId属性不为null*/
        if (CollectionUtils.isNotEmpty(unitSet)){
            /*删除unitId
            * set中不存在key，直接是删除传入的指定数据
            * 传入unitId，就删除unitId
            * */
            unitSet.remove(value.getUnitId());
        }

        /*删除创意id*/
        /*根据实体类对象获取到unitId，
        然后unitId作为key获取到索引中的Set<adId>，即creativeSet*/
        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());

        /*如果creativeSet不为null*/
        if (CollectionUtils.isNotEmpty(creativeSet)){
            /*删除creativeId
             * set中不存在key，直接是删除传入的指定数据
             * 传入AdId，就删除AdId
            * */
            creativeSet.remove(value.getAdId());
        }

    }
}
