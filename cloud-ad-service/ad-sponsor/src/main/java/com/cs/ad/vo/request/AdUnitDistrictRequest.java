package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * * 接收前端请求的请求对象--地域限制的请求对象封装
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitDistrictRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnitDistrict {
        private Long unitId;
        private String province;
        private String city;
    }
    /**使用内部类实现多个unitDistrict*/
    private List<UnitDistrict> unitDistricts;
}
