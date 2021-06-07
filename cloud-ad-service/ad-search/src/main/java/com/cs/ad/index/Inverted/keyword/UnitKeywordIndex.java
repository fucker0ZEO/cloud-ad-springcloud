package com.cs.ad.index.Inverted.keyword;

import com.cs.ad.index.IndexAware;
import com.cs.ad.index.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author fucker
 * 存在关键词限制维度的索引是倒排索引，以及一条相反的正向索引
 * 通过关键词（string类型），
 *找到记录在索引中的位置（第XX行记录，默认的主键id恰好可以表示这个位置）
 * 因此是Set<Long>类型，set限定为无重复
 * 这个索引类和普通的索引类区别较大：
 * 它是增删，都是先将索引中的key （正向索引是key）或者value （倒排索引是value），
 * 装在ConcurrentSkipListSet，这个并发set中进行去重，
 * 然后再针对这个并发set进行增删
 *
 * 一个推广单元（ID）,存在多个关键词，这点从SQL可知
 * 增删都是部分增删，即删除一个推广单元ID下的某个关键词，因此需要这种提示的增删方式
 * 单ID对应多个索引条件这点在其他限制条件中也类似
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
            /*返回空集合*/
            return Collections.emptySet();
        }
        /*根据关键词key，通过倒排索引查询unitId*/
        Set<Long> result = keywordUnitMap.get(key);

        /*如果unitId为null，需要返回出相关信息
        * 【返回值null，基本上都需要处理】
        * */
        if (result == null){
            /*返回空集合*/
            return Collections.emptySet();
        }
        /*返回非空的实体集*/
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        /*向索引中写，实质上是向数据库中写，2条索引都需要更新*/

        /*添加倒排索引*/
        /*keywordUnitMap中不存在key时，
        new一个ConcurrentSkipListSet对象--根据Key，new一个Set<Long>
        即单元id集合，可以实现并发添加*/
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                key, keywordUnitMap, ConcurrentSkipListSet::new);
        /*完成某个推广ID下添加一个关键词*/
        unitIdSet.addAll(value);

        /*添加正向索引*/
        /*遍历推广单元的ID，value就是推广单元的ID*/
        for (long unitId : value){
            /*同样方式间进行校验，不存在则new一个关键词集合
            * 可以实现并发添加多个key*/
         Set<String> keywordSet = CommonUtils.getOrCreate(
                 unitId,unitKeywordMap,ConcurrentSkipListSet::new);
         /*key添加正向索引*/
         keywordSet.add(key);
        }
    }

    @Override
    public void update(String key, Set<Long> value) {
        /*更新成本很高，不允许更新
        * 关键词的更新导致2个map发生改变
        * 而且每一个map都会牵扯到一个set，
        * 需要对set进行暴力遍历才能更新，
        * 因此不允许更新*/
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
            Set<String> keywordSet = CommonUtils.getOrCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new);

            keywordSet.remove(key);
        }

    }

    /**匹配方法
     * 传入推广单元和多个关键词，
     * 匹配推广单元是否包含了关键词*/
    public boolean match(Long unitId, List<String> keywords){
        /*判断这个map中是否包含了推广单元ID，
        且ID对应的keywordMap不为null*/
        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))){
            /*获取推广单元所对应的关键词的集合*/
            Set<String> unitKeywords = unitKeywordMap.get(unitId);
            /*当keywords是unitKeywords的子集时返回true
            * 当推广单元ID包含所有推广单元的关键词限制，
            * 才返回true*/
            return CollectionUtils.isSubCollection(keywords,unitKeywords);
        }
        return false;
    }

    public static void main(String[] args) {
        String key = "阿里";
        TreeSet<Long> value = new TreeSet<Long>();
        value.add(1L);
        value.add(2L);
        value.add(3L);
        value.add(4L);

//        System.out.println("未添加数据之前的索引map："+keywordUnitMap);
//        Set<Long> unitIds = CommonUtils.getOrCreate(
//                key, keywordUnitMap, ConcurrentSkipListSet::new
//        );
//        System.out.println("最初的ids:"+unitIds);
//        System.out.println("最初的索引map:"+keywordUnitMap);
//        unitIds.addAll(value);
//        System.out.println("之后的ids:"+unitIds);
//        System.out.println("之后的索引map:"+keywordUnitMap);
//        /*结果如下**********
//         *未添加数据之前的索引map：{}
//         *工具类最初的map:{}
//         *工具类的factory：[]
//         *返回值的map：[]
//         *工具类添加后的map：{阿里=[]}
//         *最初的ids:[]
//         *最初的索引map:{阿里=[]}
//         *之后的ids:[1, 2, 3, 4]
//         *之后的索引map:{阿里=[1, 2, 3, 4]}
//         *
//         * */


        for (long unitId : value){
            /*同样方式间进行校验，不存在则new一个关键词集合
             * 可以实现并发添加多个key*/
            Set<String> keywordSet = CommonUtils.getOrCreate(
                    unitId,unitKeywordMap,ConcurrentSkipListSet::new);
            /*key添加正向索引*/
            keywordSet.add(key);
        }
        System.out.println("正向索引的map"+unitKeywordMap);

        /**正向索引的结果如下：
         *
         * 工具类最初的map:{}
         * 工具类的factory：[]
         * 返回值的map：[]
         * 工具类添加后的map：{1=[]}
         * 工具类最初的map:{1=[阿里]}
         * 工具类的factory：[]
         * 返回值的map：[]
         * 工具类添加后的map：{1=[阿里], 2=[]}
         * 工具类最初的map:{1=[阿里], 2=[阿里]}
         * 工具类的factory：[]
         * 返回值的map：[]
         * 工具类添加后的map：{1=[阿里], 2=[阿里], 3=[]}
         * 工具类最初的map:{1=[阿里], 2=[阿里], 3=[阿里]}
         * 工具类的factory：[]
         * 返回值的map：[]
         * 工具类添加后的map：{1=[阿里], 2=[阿里], 3=[阿里], 4=[]}
         * 正向索引的map{1=[阿里], 2=[阿里], 3=[阿里], 4=[阿里]}
         *
         * */
    }
}
