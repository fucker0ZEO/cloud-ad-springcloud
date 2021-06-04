package com.cs.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cs.ad.entity.AdPlan;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IAdPlanService;
import com.cs.ad.vo.request.AdPlanGetRequest;
import com.cs.ad.vo.request.AdPlanRequest;
import com.cs.ad.vo.response.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AdPlanOPController {
    @Autowired
    private IAdPlanService adPlanService;

    /**返回值类型以vo的response为准
     * 接口名要求见名知意以service接口中的方法名为准*/
    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(
            @RequestBody
            AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: createAdPlan -> {}",
                JSON.toJSONString(request));
        return adPlanService.createAdPlan(request);
    }

    /**这里没有专门定义的response，
     * 以service接口对应的方法的返回值为准
     * 传参也是尽量和service接口保持一致*/
    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIds(
            @RequestBody
            AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor: getAdPlanByIds -> {}",
                JSON.toJSONString(request));
        return adPlanService.getAdPlanByIds(request);
    }

    /**restFul风格，更新操作需要用Put，而非post*/
    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(
            @RequestBody
            AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}",
                JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }
    /**restFul风格，删除操作需要用delete，而非post*/
    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(
            @RequestBody
            AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: deleteAdPlan -> {}",
                JSON.toJSONString(request));
//        void无需再返回，直接调用即可
        adPlanService.deleteAdPlan(request);
    }
}
