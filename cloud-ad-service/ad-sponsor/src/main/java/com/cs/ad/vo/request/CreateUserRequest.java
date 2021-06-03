package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author fucker
 * 前端请求数据的user请求对象（ViewObject层）
 * 前端传过来的JSON数据，被转化成了这个对象
 * 具体数据：username
 * 只有username需要前端提供，Token会自动生成
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String username;

/**校验username是否为null*/
    public boolean validate(){
        return !StringUtils.isEmpty(username);
    }

}
