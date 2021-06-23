package com.cs.ad.search.vo;

import com.cs.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 * 投放系统返回的响应对象
 * 返回的创意列表，即List<creative>
 *
 * 对于检索服务，它检索的对象是内存中保存的索引，而非磁盘上的DB。
 * 检索返回的结果：创意对应的索引中的记录，该记录为实体类对象。即creativeObject
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    /*填充map*/
    /*整个response定义为map
     * k为广告位的编码，
     * v为list<creative>，即创意列表。
     * 一个广告位可以展示多个广告创意，例如轮播广告，广告的来回滑动
     * 因此用list*/
    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    /**返回的创意*/
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative{
        /**广告id*/
        private Long adId;
        /**广告url*/
        private String adUrl;
        /**广告宽带和高度*/
        private Integer width;
        private Integer height;
        /**广告类型
         * 有了广告类型，
         * 媒体方才能使用对应的解码器对广告进行解码*/
        private Integer type;
        /**具体格式，例如JPG,PNG*/
        private Integer materialType;
        /*展示检测URL,广告返回给媒体方后，
        媒体方会对广告数据进行曝光，曝光即将广告展示给用户，
        这时检索系统就需要对展示的结果进行监测，
        不然就难得知媒体方已经展示了广告，难以进行后续的计费*/
        private List<String> showMonitorUrl = Arrays.asList("www.baidu.com","www.baidu.com");
        /*点击检测URL
        * 对用户点击广告，进行检测，
        * 常见的手机app上的广告一般会有点击，例如开屏广告
        * 用户进行点击后，广告系统就需要获取到用户的点击数据，
        * 告诉媒体方，广告已经被点击了*/
        private List<String> clickMonitorUrl = Arrays.asList("www.baidu.com","www.baidu.com");


        /*将索引记录对象，转化为本类中定义的creative对象*/
        public static Creative convert(CreativeObject object){
            /*new creative对象*/
            Creative creative = new Creative();

            /*将creativeObject中的数据填充到从creative对象中*/
            creative.setAdId(object.getAdId());
            creative.setAdUrl(object.getAdUrl());
            creative.setWidth(object.getWidth());
            creative.setHeight(object.getHeight());
            creative.setType(object.getType());
            creative.setMaterialType(object.getMaterialType());

            return creative;
        }
    }

}
