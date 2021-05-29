package com.cs.ad.entity;

import com.cs.ad.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "ad_user") 标记对应的数据库表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_user")
public class AdUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    /**
     * @Id 标识为主键
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 自增的方式生成主键
     * @Column(name = "id",nullable = false) 对应的数据库中的字段名为null，nullable标识是否可以为null
     */
    private Long id;

    @Basic
    @Column(name = "username",nullable = false)
    private String username;

    @Basic
    @Column(name = "token",nullable = false)
    private String token;

    @Basic
    @Column(name = "user_status",nullable = false)
    private Integer userStatus;

    @Basic
    @Column(name = "createTime",nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

    /**
     * 需要初始化的数据
     * @param username 广告主账户名
     * @param token 登录鉴权token
     */
    public AdUser(String username,String token){
        this.username = username;
        this.token = token;
//        使用枚举类中定义的status作为userStatus的数据
        this.userStatus = CommonStatus.VALID.getStatus();
        this.createTime = new Date();
//        使用当前时间作为更新时间和创建时间
        this.updateTime = this.createTime;

    }
}
