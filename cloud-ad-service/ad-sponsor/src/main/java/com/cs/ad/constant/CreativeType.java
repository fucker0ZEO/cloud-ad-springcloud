package com.cs.ad.constant;

import lombok.Getter;

/**
 * @author fucker
 */

@Getter
public enum CreativeType {

    IMAGE(1,"图片"),
    VIDED(2,"视频"),
    TEXT(3,"文本翻译");

    private int type;
    private String desc;

    CreativeType(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

}
