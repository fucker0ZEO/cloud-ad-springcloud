package com.cs.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 关键词限制
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordFeature {

    /**关键词列表*/
    private List<String> keywords;
}
