package com.cs.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "creative_unit") 标记对应的数据库表
 * 创意单元表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "creative_unit")
public class CreativeUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    /**
     * @Id 标识为主键
     * @GeneratedValue(strategy = GenerationType.IDENTITY) 自增的方式生成主键
     * @Column(name = "id",nullable = false) 对应的数据库中的字段名为null，nullable标识是否可以为null
     */
    private Long id;

    /**创意ID
     * */
    @Basic
    @Column(name = "creative_id",nullable = false)
    private Long creativeId;

    /**推广单元ID
     * */
    @Basic
    @Column(name = "unit_id",nullable = false)
    private Long unitId;

    public CreativeUnit(Long creativeId,Long unitId){
        this.creativeId = creativeId;
        this.unitId = unitId;
    }
}
