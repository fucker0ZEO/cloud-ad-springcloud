package com.cs.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "ad_unit_keyword") 标记对应的数据库表
 * 推广单元的推广维度单独分表存储
 * ad_unit_keyword 关键词推广限制
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_unit_keyword")
public class AdUnitKeyWord {
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

    @Basic
    @Column(name = "key_word",nullable = false)
    private String keyWord;

    public AdUnitKeyWord(Long unitId, String keyWord){
        this.unitId = unitId;
        this.keyWord = keyWord;
    }
}
