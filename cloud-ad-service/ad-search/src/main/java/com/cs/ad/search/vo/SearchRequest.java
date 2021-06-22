package com.cs.ad.search.vo;

import com.cs.ad.search.vo.feature.DistrictFeature;
import com.cs.ad.search.vo.feature.FeatureRelation;
import com.cs.ad.search.vo.feature.ItFeature;
import com.cs.ad.search.vo.feature.KeywordFeature;
import com.cs.ad.search.vo.media.AdSlot;
import com.cs.ad.search.vo.media.App;
import com.cs.ad.search.vo.media.Device;
import com.cs.ad.search.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * 接收投放系统的请求数据
 * 具体的数据: 媒体方的请求标识，请求的基本信息（静态内部类），
 * 请求匹配信息（限制维度，静态内部类）
 * 通过静态内部类，实现了实体类的聚合/嵌套
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    /**媒体方的请求标识*/
    private String mediaId;
    /**请求的基本信息*/
    private RequestInfo requestInfo;
    /**请求的匹配信息，即限制维度*/
    private  FeatureInfo featureInfo;

    /**具体的基本信息实体类
     * 参考投放系统的vo包下的实体类实现。
     * 投放系统的response是直接返回给广告主的响应，
     * request中很多才是对应着投放系统
     * */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo{
        /*唯一的请求ID*/
        private String requestId;
        /*广告位信息
        * 再嵌套一层实体类...难怪使用了静态内部类*/
        private List<AdSlot> adSlots;
        /*应用终端信息*/
        private App app;
        /*地理位置信息*/
        private Geo geo;
        /*设备信息*/
        private Device device;

    }

    /**具体的匹配信息实体类*/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  FeatureInfo {
        /*引入三大限制实体类*/

        /*关键词限制*/
        private KeywordFeature keywordFeature;
        /*兴趣限制*/
        private ItFeature itFeature;
        /*地域限制*/
        private DistrictFeature districtFeature;

        /*匹配之间的关系，默认值为AND。要求满足所有匹配条件*/
        private FeatureRelation relation = FeatureRelation.AND;
    }

}
