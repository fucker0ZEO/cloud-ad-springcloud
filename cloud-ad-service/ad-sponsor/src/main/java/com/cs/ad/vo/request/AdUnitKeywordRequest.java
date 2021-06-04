package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 接收前端请求的请求对象--关键词限制的请求对象封装
 * 【通过内部类实现批量创建】
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitKeywordRequest {

    /**多个keyword实现keywords，批量创建*/
    private List<UnitKeyword> unitKeywords;

    /**使用内部类实现多个unitKeyword*/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitKeyword{
        private Long unitId;
        private String keyword;
    }
}
