package com.cs.ad.client;

import com.cs.ad.client.vo.AdPlan;
import com.cs.ad.client.vo.AdPlanGetRequest;
import com.cs.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fucker
 * @Component 声明为一个组件
 * 引入hystrix主要是当下游的服务不断报错时，即时断开，不再请求下游服务
 * 实现 SponsorClient，实现对应的方法，同时将hystrix绑定到client。就像是做了一个通用的错误处理
 * 接受返回都要过一层断路器，一旦调用中发生错误，就会做服务降级（client里面声明），服务降级就会返回这里定义的错误信息
 * */

@Component
public class SponsorClientHystrix implements SponsorClient{
    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        /*断路器告诉检索系统的错误消息*/
        return new CommonResponse<>(-1,
                "eureka-client-ad-sponsor error");
    }
}
