package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adUnitKeywordTable对应着adUnitKeywordObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitKeywordTable {

    private Long unitId;
    private String keyword;
}
