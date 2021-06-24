package com.cs.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cs.ad.annotation.IgnoreResponseAdvice;
import com.cs.ad.client.SponsorClient;
import com.cs.ad.client.vo.AdPlan;
import com.cs.ad.client.vo.AdPlanGetRequest;
import com.cs.ad.search.ISearch;
import com.cs.ad.search.vo.SearchRequest;
import com.cs.ad.search.vo.SearchResponse;
import com.cs.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author fucker
 * @IgnoreResponseAdvice注解为自定义的不使用AdCommon中的统一响应注解
 */
@Slf4j
@RestController
public class SearchController {

    /**煮热service层的接口，检索服务*/
    @Autowired
    private ISearch search;

    /**注入RestTemplate*/
    @Autowired
    private RestTemplate restTemplate;
    /**注入Feign，实际工作使用的还算feign*/
    @Autowired
    private SponsorClient sponsorClient;

    /**使用ribbon调用
     * CommonResponse<List<AdPlan>>
     * 所有响应都是用common模块通一提供
     * 统一响应为：CommonResponse <T>
     *     泛型T内部才是真正的返回值类型
     *     对应的就算投放系统的 public List<AdPlan> getAdPlanByIds(
     *     投放系统的返回值是service中定义的返回给前端的值。
     *     此时的检索系统就是投放系统的前端，或者说下游
     *     上游定义了返回值，下游一直沿用。
     *     controller沿用service的返回值给VO，
     *     检索系统沿用投放系统VO的返回值类型
     *     最后return中完成检索系统调用投放系统，
     *     并且返回结果给用户（就像前端返回给用户）
     * */
    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRibbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRibbon(
            @RequestBody AdPlanGetRequest request
    ) {
        log.info("ad-search: getAdPlansByRibbon -> {}",
                JSON.toJSONString(request));
        return restTemplate.postForEntity(
                "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,
                // CommonResponse.class为提供的序列化的格式
                CommonResponse.class
                //最后通过getBody拿到返回的结果
        ).getBody();
    }

    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request
    ){
        log.info("ad-sponsor: getAdPlans -> {}",
                JSON.toJSONString(request));
//        调用注入的Feign的接口sponsorClient，像调用service一样简单
        return sponsorClient.getAdPlans(request);
    }


    /**检索广告
     *  @RequestBody 实现JSON数据到Java对象的反序列化
     * */
    @PostMapping("/fetchAds")
    public SearchResponse fetchAds( @RequestBody SearchRequest request){
//        打印日志，标记请求被调用
        log.info("ad-search: fetchAds -> {}",
                JSON.toJSONString(request));
//        调用检索方法，传入request，返回response，并返回给调用方
        return search.fetchAds(request);


    }


}
