package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 返回前端的响应对象--关键词维度的推广单元响应对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitKeywordResponse {
    /**只返回给前端主键id*/
    private List<Long> id;
}
