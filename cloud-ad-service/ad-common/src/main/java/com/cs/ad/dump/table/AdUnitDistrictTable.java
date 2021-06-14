package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adUnitDisrtrictTable对应着adUnitDistrictObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrictTable {

    private Long unitId;
    private String province;
    private String city;
}

