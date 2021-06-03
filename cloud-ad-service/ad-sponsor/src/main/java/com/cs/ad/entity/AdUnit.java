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
 * @Table(name = "ad_Unit") 标记对应的数据库表
 * AdUnit中为定义推广单元中的推广维度，
 * 推广维度单独定义了实体类,放在unit_condition包下
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_Unit")
public class AdUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    /**
     * @Id 标识为主键
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 自增的方式生成主键
     * @Column(name = "id",nullable = false) 对应的数据库中的字段名为null，nullable标识是否可以为null
     */
    private Long id;

    /**
     * 与plan表中的ID相关联的字段
     */
    @Basic
    @Column(name = "plan_id",nullable = false)
    private Long planId;

    @Basic
    @Column(name = "unit_name",nullable = false)
    private String unitName;

    @Basic
    @Column(name = "unit_status",nullable = false)
    private Integer unitStatus;

    /**
     * 【positionType 广告位类型，非常重要的字段
     * 常见的类型 开屏，贴片，中贴
     * 什么是开屏？开屏广告，刚打开APP时的广告就属于开屏广告
     * 什么是贴片？贴片广告，看视频时弹出的小弹窗是的广告就属于贴片广告
     * 什么是中贴？中贴广告，爱奇艺APP看到一半时弹出的分钟级广告就属于中贴广告
     * 参考 https://www.zhihu.com/question/286542738
     * 】
     */
    @Basic
    @Column(name = "position_type",nullable = false)
    private Integer positionType;

    /**
     * budget,广告预算
     */
    @Basic
    @Column(name = "budget",nullable = false)
    private Long budget;

    @Basic
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    /**
     * 广告推广单元更新时间
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

    public AdUnit(Long planId,String unitName,
                  Integer positionType,Long budget){
        this.planId = planId;
        this.unitName = unitName;
//        同样使用定义的状态，默认的VALID代表有效状态
        this.unitStatus = CommonStatus.VALID.getStatus();
//        创建推广单元时指定广告位的类型
        this.positionType = positionType;
        this.budget = budget;
        this.createTime = new Date();
//        使用当前时间作为更新时间和创建时间
        this.updateTime = this.createTime;
    }
}
