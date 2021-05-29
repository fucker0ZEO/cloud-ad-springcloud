package com.cs.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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


}
