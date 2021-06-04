package com.cs.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cs.ad.service.ICreativeService;
import com.cs.ad.vo.request.CreativeRequest;
import com.cs.ad.vo.response.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fucker
 * creative的controller
 * 和前面的2个adController相似，无脑写接口
 * 无需注释
 */
@RestController
@Slf4j
public class CreativeOPController {
    @Autowired
    private ICreativeService creativeService;

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(
            @RequestBody CreativeRequest request){
        log.info("ad-sponsor: createCreative -> {}",
                JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}
