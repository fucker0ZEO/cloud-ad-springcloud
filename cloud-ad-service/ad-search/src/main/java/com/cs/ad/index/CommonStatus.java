package com.cs.ad.index;

import lombok.Getter;

/**
 * @author fucker
 * 定义有些状态，无效状态
 */
@Getter
public enum CommonStatus {

    VALID(1,"有效状态"),
    INVALID(0,"无效状态");

    private Integer status;
    private String desc;

    /**全参构造*/
    CommonStatus(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }
}
