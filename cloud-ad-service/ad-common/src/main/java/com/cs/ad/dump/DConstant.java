package com.cs.ad.dump;

/**
 * @author fucker
 * 常量的文件声明，
 * 需要将数据导入到哪个目录，以及导入的目录下数据表对应的存储文件名
 */
public class DConstant {

    /**数据文件存储的目录*/
    public static final String DATA_ROOT_DIR = "E:\\IdeaProjects\\cloud-ad-springcloud\\SQL";

    /*定义各个数据表的存储文件名，每一张表一份文件
    * 文件名和索引对象一一对应*/
    /**推广计划表*/
    public static final String AD_PLAN = "ad_plan.data";
    /**推广单元表*/
    public static final String AD_UNIT = "ad_unit.data";
    /**创意表*/
    public static final String AD_CREATIVE = "ad_creative.data";
    /**创意与推广单元的关系表*/
    public static final String AD_CREATIVE_UNIT = "ad_creative_unit.data";
    /**推广单元的限制维度（兴趣）表*/
    public static final String AD_UNIT_IT = "ad_unit_it.data";
    /**推广单元的限制维度（地域）表*/
    public static final String AD_UNIT_DISTRICT = "ad_unit_district.data";
    /**推广单元的限制维度（关键词）表*/
    public static final String AD_UNIT_KEYWORD = "ad_unit_keyword.data";


}
