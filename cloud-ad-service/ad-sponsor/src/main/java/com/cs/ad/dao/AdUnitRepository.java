package com.cs.ad.dao;

import com.cs.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fucker
 * 对应推广单元表的CRUD
 */
public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    /**
     *
     *findByPlanIdAndUnitName
     * @description: 按推广计划 ID 和推广单元名称查找
     * @param planId 推广计划id
     * @param unitName 推广单元名称
     * @return: 返回对应的记录/AdUnit对象
     */
    AdUnit findByPlanIdAndUnitName(Long planId, String unitName);

    /**
     *
     *findAllByUnitStatus
     * @description: 按推广单元状态（unitStatus）查找全部。
     * @param unitStatus 推广单元状态
     *  planStatus有2种状态，有效状态和无效状态。分别对应数字1和2
     * @return: 返回对应记录/AdUnitList对象
     */
    List<AdUnit> findAllByUnitStatus(Integer unitStatus);
}
