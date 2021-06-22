package com.cs.ad.index.Inverted.district;

import com.cs.ad.index.IndexAware;
import com.cs.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

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

//    public boolean match(Long adUnitId,
//                         List<DistrictFeature.ProvinceAndCity> districts) {
//
//        if (unitDistrictMap.containsKey(adUnitId) &&
//                CollectionUtils.isNotEmpty(unitDistrictMap.get(adUnitId))) {
//
//            Set<String> unitDistricts = unitDistrictMap.get(adUnitId);
//
//            List<String> targetDistricts = districts.stream()
//                    .map(
//                            d -> CommonUtils.stringConcat(
//                                    d.getProvince(), d.getCity()
//                            )
//                    ).collect(Collectors.toList());
//
//            return CollectionUtils.isSubCollection(targetDistricts, unitDistricts);
//        }
//
//        return false;
//    }
}
