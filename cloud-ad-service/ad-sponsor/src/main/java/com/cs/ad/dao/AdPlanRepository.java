package com.cs.ad.dao;

import com.cs.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fucker
 * 继承Jpa接口
 * 2个参数：
 * AdPlan对应的实体类名，对推广计划表CRUD
 * Long类型
 * 4个查询方法
 */
public interface AdPlanRepository extends JpaRepository<AdPlan,Long> {
    /**findByIdAndUserId 根据推广计划id和用户id查询
     * @param id  推广计划id
     * @param userId 用户id
     * @return 返回对应的记录/AdPlan实体类对象
     * */
    AdPlan findByIdAndUserId(Long id, Long userId);

    /**
     *
     *findAllByIdInAndUserId
     * @description: 按 推广计划ID 和用户 ID 查找对应的记录
     * @param ids 推广计划id 可能存在多个因此是ids
     * @param userId 用户ID
     * @return: 返回对应的记录/AdPlanList
     */
    List<AdPlan> findAllByIdInAndUserId(List<Long> ids, Long userId);

    /**
     *
     *findByUserIdAndPlanName
     * @description: 按用户 ID 和计划名称查找
     * @param userId 用户ID
     * @param planName 推广计划名称
     * @return: 返回对应的记录/AdPlan实体类对象
     */
    AdPlan findByUserIdAndPlanName(Long userId, String planName);

    /**
     *
     *findAllByPlanStatus
     * @description: 根据推广计划状态（planStatus）查找所有记录
     * planStatus有2种状态，有效状态和无效状态。分别对应数字1和2
     * @param status 推广计划状态
     * @return: 返回对应的记录/AdPlanList对象
     */
    List<AdPlan> findAllByPlanStatus(Integer status);
}
