package com.cs.ad.dao.unit_condition;

import com.cs.ad.entity.unit_condition.AdUnitIt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fucker
 * 对应ad_unit_it表的CRUD
 * 不额外写其他方法时，根据主键CRUD都可以直接使用
 */
public interface AdUnitItRepository  extends JpaRepository<AdUnitIt, Long> {
}
