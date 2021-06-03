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
 * @Table(name = "ad_plan") 标记对应的数据库表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_plan")
public class AdPlan {
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
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "user_id",nullable = false)
    private Long userId;

    /**
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "plan_name",nullable = false)
    private String planName;

    /**planStatus,值来自与枚举类CommonStatus中定义的状态码
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "plan_status",nullable = false)
    private Integer planStatus;

    /**
     * 推广计划开始时间
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "start_date",nullable = false)
    private Date startDate;

    /**
     * 推广计划开始时间
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "end_date",nullable = false)
    private Date endDate;

    /**
     * 推广计划创建时间
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    /**
     * 推广计划更新时间
     * nullable = false，值不能为null
     */
    @Basic
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

    public AdPlan(Long userId,
                  String planName,
                  Date startDate,
                  Date endDate){
        this.userId = userId;
        this.planName = planName;
//        使用枚举类中定义的status作为userStatus的数据
        this.planStatus = CommonStatus.VALID.getStatus();
        this.startDate =startDate;
        this.endDate =endDate;
        this.createTime = new Date();
//        使用当前时间作为更新时间和创建时间
        this.updateTime = this.createTime;

    }

}
