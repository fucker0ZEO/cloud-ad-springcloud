package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 将推广计划返回给前端的响应对象
 * 返回的数据：id，planName
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanResponse {
    private Long id;
    private String planName;


}
