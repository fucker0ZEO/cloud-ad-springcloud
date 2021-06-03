package com.cs.ad.service;

import com.cs.ad.exception.AdException;
import com.cs.ad.vo.request.CreateUserRequest;
import com.cs.ad.vo.response.CreateUserResponse;

/**
 * @author fucker
 *
 */
public interface IUserService {
    /**
     *
     *createUserResponse
     * @description: 创建用户
     * @param request 前端user请求对象（vo包中的CreateUserRequest）
     * @return CreateUserResponse  返回给前端user响应对象（vo包中的CreateUserResponse）
     * @throws AdException 广告系统通用异常处理
     */
    CreateUserResponse createUserResponse(CreateUserRequest request)throws AdException;

}
