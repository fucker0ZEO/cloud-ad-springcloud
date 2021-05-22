package com.cs.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author fucker
 * 实现自定义过滤器
 * Zuul自带有4大过滤器
 *
 */
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
//        启动该过滤器，重置为true
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext currentContext = RequestContext.getCurrentContext();
//        请求的时间写入，可以通过RequestContext对象拿到
        currentContext.set("startTime",System.currentTimeMillis());
        return null;
    }
}
