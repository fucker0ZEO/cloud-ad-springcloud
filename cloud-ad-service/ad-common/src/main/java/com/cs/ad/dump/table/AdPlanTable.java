package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author fucker
 *
 * 字段与AdPlanObject这个索引对象一一对应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanTable {
    private Long id;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;
}