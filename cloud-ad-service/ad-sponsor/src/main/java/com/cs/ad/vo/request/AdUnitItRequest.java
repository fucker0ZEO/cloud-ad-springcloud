package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 接收前端请求的请求对象
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

    private List<UnitIt> unitIts;
}
