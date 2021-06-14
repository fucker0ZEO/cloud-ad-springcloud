package com.cs.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adCreativeUnitTable对应着adCreativeUnitObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdCreativeUnitTable {

    private Long adId;
    private Long unitId;
}
