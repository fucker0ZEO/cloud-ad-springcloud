package com.cs.ad.index.Inverted.creativeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * adId 这里的adId是存在歧义的，实际上adId是指创意Id，即creativeId
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeUnitObject {

    private Long adId;
    private Long unitId;

    /*<adId-unitId, CreativeUnitObject>*/

}
