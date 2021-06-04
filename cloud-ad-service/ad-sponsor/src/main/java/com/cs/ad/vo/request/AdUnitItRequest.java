package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 接收前端请求的请求对象--兴趣维度的推广单元请求对象
 * 允许批量创建，因此使用内部类
 * 需要接收的数据：unitId,itTag
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitIt{
        private long unitId;
        private String itTag;
    }

    /**内部类推广计划实现批量创建*/
    private List<UnitIt> unitIts;
}
