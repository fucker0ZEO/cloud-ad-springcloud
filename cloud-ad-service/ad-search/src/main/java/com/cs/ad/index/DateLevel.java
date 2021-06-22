package com.cs.ad.index;

import lombok.Getter;

/**
 * @author fucker
 * 需要维护索引的层级关系
 * 纯粹的数字，不宜维护
 */

@Getter
public enum DateLevel {

    /*总共有2，3，4层级
    * 即3个枚举*/
    LEVEL2("2","level 2"),
    LEVEL3("3","level 3"),
    LEVEL4("4","level 4");


    /**level为层级，
     * desc为层级描述信息*/
    private String level;
    private String desc;

    DateLevel(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }


}
