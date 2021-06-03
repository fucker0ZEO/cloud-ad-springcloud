package com.cs.ad.service.impl;

import com.cs.ad.constant.Constants;
import com.cs.ad.dao.AdUserRepository;
import com.cs.ad.entity.AdUser;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IUserService;
import com.cs.ad.util.CommonUtils;
import com.cs.ad.vo.request.CreateUserRequest;
import com.cs.ad.vo.response.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fucker
 * IUserService的实现类
 * @Service注解 标记为JavaBean
 * @Transactional注解 向数据库中写数据时，需要开启事务
 * 主要逻辑：
 * 校验请求对象为null，
 * 通过username查询SQL校验DB内是否已存在该行记录（AdUser）
 * 通过username和token，调用DAO接口的save写入记录，并获取到返回对象newUser
 * new CreateUserResponse对象（通过newUser的get方法传值），返回CreateUserResponse对象
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    /**注入user的DAO接口*/
    private final AdUserRepository userRepository;
    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CreateUserResponse createUserResponse(CreateUserRequest request)
            throws AdException {
//        判断请求对象是否为null
        if (!request.validate()){
//            常量类返回请求参数异常
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        根据request中传过来的username，使用DAO接口从数据库中查找AdUser对象
        AdUser oldUser = userRepository.findByUsername(request.getUsername());

        if (oldUser != null){
//            常量类返回同名用户已存在的异常
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        /**newUser
         * 根据request中传入的username，
         * 以及将 username通过工具类md5转化成token，
         * 调用user的DAO接口中的save方法，向ad_user表中插入一行记录
         *
         * ad_user表有6个字段:
         * id：通过自增策略生成
         * username: 通过request传入
         * token：对username进行md5生成
         * create_time: 使用当前时间
         * update_time: 同样使用当前时间
         * user_status: CommonStatus枚举类定义通用状态码
         *
         * 实体类定义了2个参数的构造函数，传入token和username，
         * 其余的数据构造函数补全得到AdUser对象，
         * 最后将AdUser对象作为数据传入save方法进行SQL插入
         * save返回值为AdUser对象，因此拿到了user对象--newUser
         * */
        AdUser newUser = userRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.md5(request.getUsername())
        ));
        /**通过newUser的get方法，
         * 拿到id,name,token,createTime,updateTime数据，写入CreateUserResponse
         * 最后返回CreateUserResponse对象
         * */
        return new CreateUserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getToken(),
                newUser.getCreateTime(),
                newUser.getUpdateTime()
        );
    }
}
