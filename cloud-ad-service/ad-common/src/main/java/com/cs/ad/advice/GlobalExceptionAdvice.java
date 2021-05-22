package com.cs.ad.advice;

import com.cs.ad.exception.AdException;
import com.cs.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fucker
 * 自定义的异常捕获处理类
 * 异常也是消息，因此也需要同一响应处理
 * 发生异常后，拿不到response，但是能够拿到异常对象
 * @ExceptionHandler(value = AdException.class) 告诉spring要自己处理的具体异常，当发生AdException时，就会由这个类处理
 * 将AdException中定义的message，set进Data中
 * code为-1，message为"business error"set进CommonResponse中,最后返回commonResponse
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = AdException.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest request, AdException adException){
        CommonResponse<String> commonResponse = new CommonResponse<>(-1,"business error");
        commonResponse.setData(adException.getMessage());
        return commonResponse;
    }
}
