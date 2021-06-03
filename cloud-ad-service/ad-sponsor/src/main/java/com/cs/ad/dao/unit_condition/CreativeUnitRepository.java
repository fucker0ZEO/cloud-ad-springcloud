package com.cs.ad.dao.unit_condition;

import com.cs.ad.entity.unit_condition.CreativeUnit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fucker
 * 对应creative_unit表的CRUD
 * 不额外写其他方法时，根据主键CRUD都可以直接使用
 */
public interface CreativeUnitRepository  extends JpaRepository<CreativeUnit, Long> {
}
