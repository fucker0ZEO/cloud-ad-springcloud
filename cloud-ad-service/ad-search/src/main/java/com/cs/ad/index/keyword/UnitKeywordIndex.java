package com.cs.ad.index.keyword;

import com.cs.ad.index.IndexAware;
import com.cs.ad.index.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author fucker
 * 存在关键词限制维度的索引是倒排索引，以及一条相反的正向索引
 * 通过关键词（string类型），
 *找到记录在索引中的位置（第XX行记录，默认的主键id恰好可以表示这个位置）
 * 因此是Set<Long>类型
 */
@Component
@Slf4j
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
    /**存在两条索引，一正向，一倒排
     * keywordUnitMap 根据keyword找到unit--倒排索引
     * unitKeywordMap 根据unitId找到关键词--正向索引
     * */
    private static Map<String, Set<Long>> keywordUnitMap;
    private static Map<Long, Set<String>> unitKeywordMap;

    static {
        //根据关键词找到推广单元 id 的倒排索引
        keywordUnitMap = new ConcurrentHashMap<>();
        //根据推广单元 id 找到关键词的正向索引，一个推广单元也可以设置很多个限制
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    /**通过关键词获取unitId
     * 倒排索引*/
    @Override
    public Set<Long> get(String key) {
        /*如果关键词为null，代表无法命中任何推广单元id*/
        if (StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        /*根据关键词key，通过倒排索引查询unitId*/
        Set<Long> result = keywordUnitMap.get(key);

        /*如果unitId为null，需要返回出相关信息
        * 【返回值null，基本上都需要处理】
        * */
        if (result == null){
            return Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        /*向索引中写，实质上是向数据库中写，2条索引都需要更新*/

        /*添加倒排索引*/
        Set<Long> unitIdSet = CommonUtils.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        /*完成添加操作*/
        unitIdSet.addAll(value);

        /*添加正向索引*/
        for (long unitId : value){
         Set<String> keywordSet = CommonUtils.getOrCreate(
                 unitId,unitKeywordMap,ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }
    }

    @Override
    public void update(String key, Set<Long> value) {
        /*更新成本很高，不允许更新
        * 关键词的更新导致2个map发生改变
        * 而且每一个map都会牵扯到一个set，
        * 需要对set进行暴力遍历才能更新，因此不允许更新*/
        log.error("");
    }

    @Override
    public void delete(String key, Set<Long> value) {

        /*删除可能只是删除部分，因此需要先获取ids*/
        Set<Long> unitIds = CommonUtils.getOrCreate(
                key, keywordUnitMap, ConcurrentSkipListSet::new
        );
        /*实现删除倒排索引*/
        unitIds.removeAll(value);

        /*实现删除正向索引*/
        for (long unitId : value){
            Set<String> orCreate = CommonUtils.getOrCreate(
                    unitId, unitKeywordMap, ConcurrentSkipListSet::new);
        }
    }

    /**匹配方法*/
    public boolean match(Long unitId, List<String> keywords){
        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            /*当keywords是unitKeywords的子集时返回true*/
            return CollectionUtils.isSubCollection(keywords,unitKeywords);
        }
        return false;
    }
}
