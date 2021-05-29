package com.cs.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "ad_unit_district") 标记对应的数据库表
 * 推广单元的推广维度单独分表存储
 * ad_unit_district 地域推广限制。
 * 其中有省和市，两个代表地域的字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_unit_district")
public class AdUnitDistrict {
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
     * 关联推广单元
     */
    @Basic
    @Column(name = "unit_id",nullable = false)
    private Long unitId;

    /**
     * province 代表省。地域限制中的省
     */
    @Basic
    @Column(name = "province",nullable = false)
    private String province;

    /**
     * city 代表市，地域限制中的市
     */
    @Basic
    @Column(name = "city",nullable = false)
    private String city;

    public AdUnitDistrict(Long unitId, String province,String city){
        this.unitId = unitId;
        this.province = province;
        this.city = city;
    }
}
