package com.cs.ad.advice;

import com.cs.ad.annotation.IgnoreResponseAdvice;
import com.cs.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author fucker
 * Advice 拦截
 *@RestControllerAdvice 对响应实现统一拦截
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("all")
/**返回值是Boolean类型，表示判断是否该拦截这个响应
 * 根据类判断，根据方法判断
 * */
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        /**如果类上面有注解
         * 就返回false，不拦截
         * 不受统一拦截的影响
         * 通过传参methodParameter的getDeclaringClass()方法拿到那个需要拦截的类
         * 再使用isAnnotationPresent，判断是否存在指定类型的注解
         * 指定的自定义注解：IgnoreResponseAdvice.class
         * 没有这个注解，即if(false)，直接返回true，直接拦截
         * */
        if(methodParameter.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }
        /**如果方法上面有注解
         * 就返回false，不拦截
         * 不受统一拦截的影响
         * 和上面的类似，判断方法上面是否存在相应的注解
         * 存在 return false，反之为true*/
        if (methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        ))
            return false;

        return true;
    }

    @Nullable
    @Override
/**before body write 写入响应之前，见名知意
 * 表示写入响应之前可以做的一些操作,即拦截commonResponse。
 *
 * */
    public Object beforeBodyWrite(@Nullable Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest ServerHttpRequest,
                                  ServerHttpResponse ServerHttpResponse) {
        CommonResponse<Object> response =new CommonResponse<>(0,"");
//       o为响应对象，当o为nulll时,值已经为null了，无需再拦截，可直接返回
        if (o == null){
            return response;
//            o instanceof CommonResponse,即O是commonResponse对象，无需处理，直接返回
        }else if (o instanceof CommonResponse){
            response = (CommonResponse<Object>) o;
        }else {
//            如果不是前2者，将o，set进自定义的Data属性中
            response.setData(o);
        }
        return null;
    }
}
