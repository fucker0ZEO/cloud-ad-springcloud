package com.cs.ad.entity.unit_condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author fucker
 * @Entity 标记为实体类
 * @Table(name = "ad_unit_it") 标记对应的数据库表
 * 推广单元的推广维度单独分表存储
 * ad_unit_it 兴趣限制。这个it命名并不好，it在这个项目中应该都是代表兴趣
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ad_unit_it")
public class AdUnitIt {
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
     * 用来标识兴趣的标签
     * 例如常见的篮球标签，音乐标签
     */
    @Basic
    @Column(name = "it_tag",nullable = false)
    private String itTag;

    public AdUnitIt(Long id, String itTag){
        this.id = id;
        this.itTag = itTag;
    }
}
