package com.cs.ad.index.Inverted.district;

import com.cs.ad.index.IndexAware;
import com.cs.ad.search.vo.feature.DistrictFeature;
import com.cs.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * @author fucker
 *  一个推广单元（ID）,存在多个地域条件，这点从SQL可知
 *  增删都是部分增删，即删除一个推广单元ID下的某个地域条件，因此需要这种提示的增删方式
 *  单ID对应多个索引条件这点在其他限制条件中也类似
 */
@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static Map<String, Set<Long>> districtUnitMap;
    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    /**match方法，实现匹配
     * districts 为多个ProvinceAndCity实体类对象组成的集合
     * ProvinceAndCity类似于被嵌套的实体类对象。其中有省和市2个属性
     * */
    public boolean match(Long adUnitId,
                         List<DistrictFeature.ProvinceAndCity> districts){
//        校验ProvinceAndCity实体类对象中省或者市都不为为null
        if (unitDistrictMap.containsKey(adUnitId)
                && CollectionUtils.isEmpty(unitDistrictMap.get(adUnitId)) ){
//            根据索引map的get方法+get传入的adUnitId,`返回对应的数据记录对象
            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
//            targetDistricts为目标地区。
//            将ProvinceAndCity实体类中的省和市2个属性拼接为一个字符串，
//            存入targetDistricts属性中
            List<String> targetDistricts = districts.stream()
                    .map(
//                            stringConcat()为自定义的字符串拼接方法
//                            省和市2个属性，拼接为1个字符串
                            d -> CommonUtils.stringConcat(
                                    d.getProvince(), d.getCity()
                            )
//                            map转变为list
                    ).collect(Collectors.toList());
//            校验目标地区是否是unitDistricts的子集合
            return CollectionUtils.isSubCollection(targetDistricts, unitDistricts);
        }
//
        return false;
    }


    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> value) {

        log.info("UnitDistrictIndex, before add: {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.addAll(value);

        for (Long unitId : value) {

            Set<String> districts = CommonUtils.getOrCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districts.add(key);
        }

        log.info("UnitDistrictIndex, after add: {}", unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> value) {

        log.error("district index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        log.info("UnitDistrictIndex, before delete: {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, districtUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {

            Set<String> districts = CommonUtils.getOrCreate(
                    unitId, unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            districts.remove(key);
        }

        log.info("UnitDistrictIndex, after delete: {}", unitDistrictMap);
    }
}
