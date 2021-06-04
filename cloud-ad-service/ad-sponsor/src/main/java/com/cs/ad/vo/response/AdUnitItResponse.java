package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 返回前端的响应对象--兴趣维度的推广单元响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitItResponse {

    /**只返回给前端主键id*/
    private List<Long> ids;
}
