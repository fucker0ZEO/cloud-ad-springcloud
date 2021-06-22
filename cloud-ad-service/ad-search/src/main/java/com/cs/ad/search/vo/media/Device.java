package com.cs.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 设备信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    /**设备id，或者说设备编码*/
    private String deviceCode;

    /**设备的mac地址*/
    private String mac;

    /**设备的IP*/
    private String ip;

    /**机型编码*/
    private String model;

    /**屏幕分辨率尺寸*/
    private String displaySize;

    /**屏幕尺寸*/
    private String screenSize;

    /**设备序列号*/
    private String serialName;

}
