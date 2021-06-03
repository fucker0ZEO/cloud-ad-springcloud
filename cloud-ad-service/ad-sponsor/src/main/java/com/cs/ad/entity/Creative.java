package com.cs.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "ad_creative") 标记对应的数据库表
 * ad_creative 代表广告创意，或者说是具体的广告数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_creative")
public class Creative {
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
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * type具体的广告数据类型，例如文本，视频，图片
     */
    @Basic
    @Column(name = "type",nullable = false)
    private Integer type;

    /***
     *物料的数据格式，例如type标识物料类型，而图片可以分为很多格式
     * 例如jpg,png
     */

    @Basic
    @Column(name = "material_type",nullable = false)
    private Integer materialType;

    /**物料的高度*/
    @Basic
    @Column(name = "height",nullable = false)
    private Integer height;

    /**物料的宽度*/
    @Basic
    @Column(name = "width",nullable = false)
    private Integer width;

    /**物料大小*/
    @Basic
    @Column(name = "size",nullable = false)
    private  Long size;

    /**广告持续时长*/
    @Basic
    @Column(name = "duration",nullable = false)
    private Integer duration;

    /**审核状态 广告未必可以播放。满足审核状态才能播放*/
    @Basic
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    /**每一个物料都是用户上传的，因此有用户信息
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "user_id",nullable = false)
    private Long userId;

    /**广告数据的URL*/
    @Basic
    @Column(name = "url",nullable = false)
    private String url;


    @Basic
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time",nullable = false)
    private Date updateTime;



}
