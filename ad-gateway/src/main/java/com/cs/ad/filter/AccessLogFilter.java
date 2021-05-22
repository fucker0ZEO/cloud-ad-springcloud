package com.cs.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
//        过滤器最后执行
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
//        读取写入的请求时间，后面打印请求时间
        Long startTime = (Long) currentContext.get("startTime");
        long duration = System.currentTimeMillis() - startTime;

//        日志打印请求的URI，获取请求对象
        HttpServletRequest request = currentContext.getRequest();
//        获取请求URI
        String uri = request.getRequestURI();

//        info日志打印输出。。时间单位，毫秒
        log.info("uri: "+uri + ",duration: " + duration /100 + "ms");

        return null;
    }
}
