package com.cs.ad.controller;

import com.alibaba.fastjson.JSON;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IUserService;
import com.cs.ad.vo.request.CreateUserRequest;
import com.cs.ad.vo.response.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fucker
 * @RequestBody注解实现反序列化
 */
@RestController
@Slf4j
public class UserOPController {

    @Autowired
    private IUserService userService;

    @PostMapping("/create/user")
    public CreateUserResponse createUser(
            @RequestBody
            CreateUserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));
//        直接将request传给service中的createUserResponse，不做其他操作
        return userService.createUserResponse(request);
    }
}
