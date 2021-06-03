package com.cs.ad.dao;

import com.cs.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fucker
 * 对应创意表（具体的广告数据表）
 * 不额外写其他方法时，根据主键CRUD都可以直接使用
 */
public interface CreativeRepository extends JpaRepository<Creative, Long> {

}