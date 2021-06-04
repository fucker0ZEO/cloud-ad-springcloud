package com.cs.ad.service;

import com.cs.ad.entity.AdPlan;
import com.cs.ad.exception.AdException;
import com.cs.ad.vo.request.AdPlanGetRequest;
import com.cs.ad.vo.request.AdPlanRequest;
import com.cs.ad.vo.response.AdPlanResponse;

import java.util.List;

/**
 * @author fucker
 * 对推广计划（AdPlan）的CRUD
 */
public interface IAdPlanService {
    /**
     *
     *createAdPlan
     * @description: 创建推广计划
     * @param request  adPlan的前端请求对象
     * @return createAdPlan 创建adPlan的响应对象
     * @throws AdException 广告系统通用异常处理
     */
    AdPlanResponse createAdPlan(AdPlanRequest request)throws AdException;


    /**
     *
     *getAdPlanByIds
     * @description: 根据ids获取推广计划
     * @param request adPlan的前端请求对象
     * @return AdPlan adPlan的前端响应对象
     * @throws AdException 广告系统通用异常处理
     */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request)throws AdException;

    /**
     *
     *updateAdPlan
     * @description:
     * @param request adPlan的前端请求对象
     * @return updateAdPlan
     * @throws AdException
     */
    AdPlanResponse updateAdPlan(AdPlanRequest request)throws AdException;

    /**
     *
     *deleteAdPlan
     * @description: 删除推广计划
     * @param request adPlan的前端请求对象
     * @return void 无固定返回值类型
     * @throws AdException
     */
    void deleteAdPlan(AdPlanRequest request)throws AdException;
}
