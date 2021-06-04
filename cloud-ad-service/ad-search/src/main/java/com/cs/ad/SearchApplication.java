package com.cs.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author fucker
 * @EnableEurekaClient  该注解为标识为Eureka的client端
 * @EnableHystrix 该注解为熔断器
 * @EnableHystrixDashboard 该注解为数据监控
 * @EnableFeignClients 该注解为使用Feign去调用其他微服务
 * @EnableCircuitBreaker  该注解为数据可视化
 * @EnableDiscoveryClient 该注解为服务发现
 */
@EnableDiscoveryClient
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }

    /**    使用Ribbon，需要使用RestTemplate客户端，
     * 定义客户端并注册到容器中
     * @LoadBalanced 该注解为开启负载均衡，默认是轮询实现负载均衡
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
