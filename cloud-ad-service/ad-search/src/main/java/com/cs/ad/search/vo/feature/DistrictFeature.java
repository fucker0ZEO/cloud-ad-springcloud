package com.cs.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 地域限制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature {

    /**静态内部类，地域中包含有省和市两个属性*/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceAndCity{
        private String province;
        private String city;
    }

    /**地域限制列表，类型为静态内部类对象
     * 再嵌套一个实体类
     * */
    private List<ProvinceAndCity> districts;
}
