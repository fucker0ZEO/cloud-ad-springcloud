package com.cs.ad.constant;

import lombok.Getter;

/**
 * constant，常量包
 *
 * @author fucker  CommonStatus 通用状态码定义枚举
 */
@Getter
public enum CommonStatus {

    /**
     * Invalid common status.
     */
    VALID(1,"有效状态"),
    INVALID(2,"无效状态");
    private final Integer status;
    private final String desc;
    CommonStatus(Integer status,String desc){
        this.status = status;
        this.desc = desc;
    }


}
