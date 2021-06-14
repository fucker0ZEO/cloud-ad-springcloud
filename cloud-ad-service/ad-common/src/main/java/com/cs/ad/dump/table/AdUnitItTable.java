package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adUnitItTable对应着adUnitItObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItTable {

    private Long unitId;
    private String itTag;
}

