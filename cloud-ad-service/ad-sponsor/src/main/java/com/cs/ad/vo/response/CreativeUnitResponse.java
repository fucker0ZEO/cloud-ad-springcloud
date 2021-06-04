package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * creativeUnit的响应的对象
 * 返回的数据：creative_unit表的主键，ids
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeUnitResponse {

    private List<Long> ids;
}
