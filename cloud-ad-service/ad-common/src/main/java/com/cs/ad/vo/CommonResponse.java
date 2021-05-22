package com.cs.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fucker
 * 不知道响应对象的类型，因此指定为泛型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse <T> implements Serializable {

    private Integer code;
    private String message;
    /**不知道类型的响应数据data
     * 拦截器拦截下来的响应对象O，会被set进data中*/
    private T data;

    /**额外定义一个包含message和code的构造函数*/
    public CommonResponse(Integer code,String message){
        this.code = code;
        this.message =message;
    }
}
