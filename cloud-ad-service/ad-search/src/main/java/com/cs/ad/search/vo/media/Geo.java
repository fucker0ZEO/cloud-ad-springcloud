package com.cs.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 地理位置信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

    /**维度和精度*/
    private Float latitude;
    private Float longitude;
    /**所在的城市*/
    private String city;

    /**所在的省份*/
    private String province;

}
