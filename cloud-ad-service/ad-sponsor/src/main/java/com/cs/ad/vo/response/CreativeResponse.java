package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 响应对象，返回的数据为id和name
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeResponse {

    private Long id;
    private String name;
}
