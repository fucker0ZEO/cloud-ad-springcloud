package com.cs.ad.exception;

/**
 * @author fucker
 * 自定义的统一异常处理类
 */
public class AdException extends Exception{
    public AdException(String message){
        super(message);
    }
}
