package com.cs.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 兴趣限制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItFeature {

    /**兴趣列表*/
    private List<String> its;
}
