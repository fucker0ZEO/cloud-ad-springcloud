package com.cs.ad.search.impl;

import com.cs.ad.index.DataTable;
import com.cs.ad.index.Inverted.district.UnitDistrictIndex;
import com.cs.ad.index.Inverted.interest.UnitItIndex;
import com.cs.ad.index.Inverted.keyword.UnitKeywordIndex;
import com.cs.ad.index.adUnit.AdUnitIndex;
import com.cs.ad.search.ISearch;
import com.cs.ad.search.vo.SearchRequest;
import com.cs.ad.search.vo.SearchResponse;
import com.cs.ad.search.vo.feature.DistrictFeature;
import com.cs.ad.search.vo.feature.FeatureRelation;
import com.cs.ad.search.vo.feature.ItFeature;
import com.cs.ad.search.vo.feature.KeywordFeature;
import com.cs.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author fucker
 * search服务的Service层实现类
 */
@Slf4j
@Component
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest searchRequest) {
//        获取请求中的的广告位信息
        List<AdSlot> adSlots = searchRequest.getRequestInfo().getAdSlots();
//        获取请求中的三个Feature,后面根据请求进过滤
        KeywordFeature keywordFeature = searchRequest.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = searchRequest.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = searchRequest.getFeatureInfo().getItFeature();
//        获取请求中Feature之间的关系，是and，还是or
        FeatureRelation featureRelation = searchRequest.getFeatureInfo().getRelation();

//        构造响应对象,new searchResponse
        SearchResponse searchResponse = new SearchResponse();
//       searchResponse 初始化了adSlot2Ads这个map
//        k位广告位编码，v为创意列表
        Map<String,List<SearchResponse.Creative>> adSlot2Ads = searchResponse.getAdSlot2Ads();

//        遍历adSlot,拿到其中具体的广告位信息
        for (AdSlot adSlot : adSlots){
            Set<Long> targetUnitIdSet;

            /*传统的方式是
            * 注入DAO接口
            * 调用DAO接口提供的查询方法+传参
            * DAO接口调用SQL去DB中查询数据
            * 返回对应的记录。
            *
            * 现在DataTable.of()，替代了注入DAO接口
            * 索引对象直接提供方法查询，替代了DAO查询方法+DAO调SQL
            * 内存索引替代了DB
            * */
//            根据流量类型，获取初始的adUnit，即adUnitIds。
//            作用是预过滤（流量类型校验来实现预过滤），缩小可能的范围

//            通过DataTable.of(AdUnitIndex.class)，获取到对应的unit索引对象
            Set<Long> adUnitIdSet = DataTable.of(
                    AdUnitIndex.class
//                    调用索引对象中的match()方法，
//                    根据传入的流量类型获取到adUnitIds
            ).match(adSlot.getPositionType());
//            and，如何实现过滤？
            if (featureRelation == featureRelation.AND){
//                调用定义的3个过来方法进行过滤。根据请求中的限制维度参数实现过滤
                //每一次过滤，idSet中都可能要id被移除
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterDistrictFeature(adUnitIdSet,districtFeature);
                filterItTagFeature(adUnitIdSet,itFeature);
//                拿到完成3充过滤后的idSet
                targetUnitIdSet = adUnitIdSet;

                //否则进行or过滤，满足一个即可
            }else{
//                调用OR过滤方法，传入3个过滤参数和IDSet，返回过滤后的Set集合
                targetUnitIdSet = getORRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        itFeature
                );

            }
        }


        return null;
    }





    /**关键词过滤方法*/
    private void filterKeywordFeature(
//            对ids，进行关键词过滤
            Collection<Long> adUnitIds,KeywordFeature keywordFeature){
//        校验，传入的ids是否为null
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }
//        校验传入的keywords是否为null。关键词不为空才需要过滤
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){

//            在ids中移除，校验返回false的id，即可实现过来
            CollectionUtils.filter(
                    adUnitIds,
//                    根据索引对象的match方法进行校验，
//                    返回false的id会在ids中被移除
                    adUnitId ->
                            DataTable.of(UnitKeywordIndex.class)
//                                    根据id和关键词进行校验
                                    .match(adUnitId,
                                            keywordFeature.getKeywords())
            );
        }
    }


    /**过滤标签，参考上面的关键词过滤*/
    private void filterItTagFeature(Collection<Long> adUnitIds,
                                    ItFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {

            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitItIndex.class)
                                    .match(adUnitId,
                                            itFeature.getIts())
            );
        }
    }

    /**过滤地区，参考关键词过滤，不再赘述*/
    private void filterDistrictFeature(
            Collection<Long> adUnitIds, DistrictFeature districtFeature
    ) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {

            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId ->
                            DataTable.of(UnitDistrictIndex.class)
                                    .match(adUnitId,
                                            districtFeature.getDistricts())
            );
        }
    }


    /**根据OR关系进行过滤
     * 传入3个参数
     * 3个参数保存为副本
     * 副本依次过滤，然后进行union取并集
     * 最后返回并集进行过滤
     * */
    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }

        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterItTagFeature(itUnitIdSet, itFeature);

        return new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(keywordUnitIdSet, districtUnitIdSet),
                        itUnitIdSet
                )
        );
    }
}
