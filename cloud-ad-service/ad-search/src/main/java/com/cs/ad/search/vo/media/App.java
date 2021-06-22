package com.cs.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * 终端信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class App {

    /**应用编码*/
    private String appCode;
    /**应用名称*/
    private String appName;

    /**应用包名*/
    private String packageName;

    /**应用请求页面名*/
    private String activityName;

}
