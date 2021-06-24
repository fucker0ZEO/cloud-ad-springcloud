package com.cs.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.cs.ad.index.CommonStatus;
import com.cs.ad.index.DataTable;
import com.cs.ad.index.Inverted.creativeUnit.CreativeUnitIndex;
import com.cs.ad.index.Inverted.district.UnitDistrictIndex;
import com.cs.ad.index.Inverted.interest.UnitItIndex;
import com.cs.ad.index.Inverted.keyword.UnitKeywordIndex;
import com.cs.ad.index.adUnit.AdUnitIndex;
import com.cs.ad.index.adUnit.AdUnitObject;
import com.cs.ad.index.creative.CreativeIndex;
import com.cs.ad.index.creative.CreativeObject;
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
//            根据unitIdSet获取unitObjects
            List<AdUnitObject> unitObjects =
//                    先获取到索引map对象，再调用其中的fetch()获取对象
                    DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
//            校验状态是否有效,实现对有效状态的过滤
            filterAdUnitAndPlanStatus(unitObjects,CommonStatus.VALID);
//            根据推广单元列表获取到创意id列表。“联表查询第一步”
            List<Long> adIds = DataTable.of(CreativeUnitIndex.class)
                    .selectAds(unitObjects);
//            根据创意id列表获取到创意对象列表。“联表查询第2步”
            List<CreativeObject> creatives = DataTable.of(CreativeIndex.class)
                    .fetch(adIds);

//            通过AdSlot中的宽度，高度，流量类型实现对创意对象列表进行过滤
            filterCreativeByAdSlot(
                    creatives,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );
//            将经过过滤后的creatives变为一个list，实现单广告位单创意
//            填充adSlot2Ads这个映射关系map.
//            k是广告位编码，v是vo包下的创意对象
            adSlot2Ads.put(
                    adSlot.getAdSlotCode(),
                    buildCreativeResponse(creatives)
            );

        }

//      打印请求对象和响应对象的log
        log.info("fetchAds: {}-{}",
                JSON.toJSONString(searchRequest),
                JSON.toJSONString(searchResponse));


//        返回response
        return searchResponse;
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

    /**根据status来对unitObjects进行过滤
     * 不符合条件则从推广单元列表中移除该推广单元*/
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects,
                                           CommonStatus status){
//        校验推广单元列表是否为努力了，为null则返回空
        if (CollectionUtils.isEmpty(unitObjects)){
            return;
        }
//        实现过滤
        CollectionUtils.filter(
//                想要过滤袋集合类型
                unitObjects,
//                函数的返回值为需要移除的推广单元对象
//                传入的status和推广单元中的status,进行比较
                object -> object.getUnitStatus().equals(status.getStatus())
//                        且推广单元中的推广计划的status进行比较
                        && object.getAdPlanObject().getPlanStatus().equals(status.getStatus())
        );


    }

    /**通过AdSlot实现对创意对象列表进行过滤。
     * 即根据宽度，高度，审核状态,流量类型进行过滤
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creatives,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type){
//        传参校验，creatives是否为null
        if (CollectionUtils.isEmpty(creatives)){
            return;
        }

        CollectionUtils.filter(
                creatives,
                creative ->
//                        依次校验审核状态，宽，高，流量类型。
//                        如有false则移除列表中的该creative
                        creative.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                                && creative.getWidth().equals(width)
                                && creative.getHeight().equals(height)
                                && type.contains(creative.getType())
        );
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creatives){

//        传参校验creative列表是否为null
        if (CollectionUtils.isEmpty(creatives)){
            return Collections.emptyList();
        }

//        从创意列表中随机获取创意对象
        CreativeObject randomObject = creatives.get(
                Math.abs(new Random().nextInt()) % creatives.size()
        );
//        randomObject转化为对应的vo对象，并加入列表，最后将列表返回
        return Collections.singletonList(
//                调用convert方法，将creativeObject转化为vo.creative对象
                SearchResponse.convert(randomObject)
        );
    }

}
