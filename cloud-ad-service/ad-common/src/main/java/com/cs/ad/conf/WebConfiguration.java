package com.cs.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author fucker
 * 定制自己的HTTP消息转换器
 * 将Java的响应对象转换为HTTP数据输出流。
 * 即将Java的Jackson库输出成json格式
 * 当有多个转化器时，会选择最合适的转化器使用
 * 不同的转换器支持不同的数据格式
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        清空转化器list
        converters.clear();
//        增加一个转换器。使用MappingJackson2作为转化器
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
