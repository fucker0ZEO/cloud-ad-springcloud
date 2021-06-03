package com.cs.ad.dao;

import com.cs.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fucker
 * 对应用户表的CRUD
 */
public interface AdUserRepository extends JpaRepository<AdUser,Long> {

    /**
     *
     *findByUsername
     * @description: 根据用户名查找用户记录
     * @param username 用户名
     * @return: 返回对应的记录/返回AdUser实体类对象
     */
    AdUser findByUsername(String username);
}
