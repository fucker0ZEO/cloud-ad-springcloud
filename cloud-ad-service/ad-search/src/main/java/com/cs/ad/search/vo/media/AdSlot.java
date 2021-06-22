package com.cs.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 广告位信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdSlot {

    /**广告位的编码*/
    private String adSlotCode;

    /**流量类型，例如视频流量，图片流量，文本流量*/
    private Integer positionType;

    /**广告位的宽和高*/
    private Integer width;
    private Integer height;


    /**广告位的物料类型，例如图片中的JPG,PNG格式*/
    private List<Integer> type;

    /**广告位的最低价格*/
    private Integer minCpm;
}

