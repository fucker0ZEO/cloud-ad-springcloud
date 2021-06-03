package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 将推广单元返回给前端的响应对象
 * 返回的数据：id，unitName
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitResponse {
    private Long id;
    private String unitName;

}
