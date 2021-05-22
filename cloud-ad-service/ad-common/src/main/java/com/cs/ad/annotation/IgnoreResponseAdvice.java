package com.cs.ad.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fucker
 * @Target({ElementType.TYPE,ElementType.METHOD}) 使用中TYPE和method上面。type代表类，method代表方法
 *@Retention(RetentionPolicy.RUNTIME) 使用的时间：使用在runtime（运行时）
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
