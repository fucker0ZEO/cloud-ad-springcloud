package com.cs.ad.client;

import com.cs.ad.client.vo.AdPlan;
import com.cs.ad.client.vo.AdPlanGetRequest;
import com.cs.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author fucker
 * 使用Feign作为服务注册的接口
 * @FeignClient(value = "eureka-client-ad-sponsor")：
 * 作为client端使用feign,value代表所调用的服务
 */
@FeignClient(value = "eureka-client-ad-sponsor")
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/adPlan", method = RequestMethod.POST)
    /**
     *getAdPlans
     * @description: 获取AdPlan
     * @param request
     * @return com.cs.ad.vo.CommonResponse<java.util.List<com.cs.ad.client.vo.AdPlan>>
     */
    CommonResponse<List<AdPlan>> getAdPlans(
                @RequestBody AdPlanGetRequest request
                );
        }

