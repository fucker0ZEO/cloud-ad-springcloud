package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adUnitTable对应着adUnitObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitTable {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;

    private Long planId;
}
