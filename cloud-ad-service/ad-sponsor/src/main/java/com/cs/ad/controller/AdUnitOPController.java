package com.cs.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IAdUnitService;
import com.cs.ad.vo.request.*;
import com.cs.ad.vo.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fucker
 * Unit相关的Controller
 * 无脑写接口和adPlan非常相似，因此无注释
 */
@RestController
@Slf4j
public class AdUnitOPController {
    @Autowired
    private IAdUnitService adUnitService;

    @PostMapping("/create/unit")
    public AdUnitResponse createUnit(
            @RequestBody
            AdUnitRequest request) throws AdException {
        log.info("ad-sponsor: createUnit -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnit(request);
    }

    @PostMapping("/create/unitKeyword")
    public AdUnitKeywordResponse createUnitKeyword(
            @RequestBody
            AdUnitKeywordRequest request) throws AdException {
        log.info("ad-sponsor: createUnitKeyword -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnitKeyword(request);
    }
    @PostMapping("/create/unitIt")
    public AdUnitItResponse createUnitIt(
            @RequestBody
            AdUnitItRequest request) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(
            @RequestBody AdUnitDistrictRequest request) throws AdException {
        log.info("ad-sponsor: createUnitDistrict -> {}",
                JSON.toJSONString(request));
        return adUnitService.createUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public CreativeUnitResponse createCreativeUnit(
            @RequestBody CreativeUnitRequest request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}",
                JSON.toJSONString(request));
        return adUnitService.createCreativeUnit(request);
    }
}
