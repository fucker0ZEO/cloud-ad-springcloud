package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author fucker
 * 接受前端传过来推广单元的请求对象
 * 需要接收的数据：planId,unitName,positionType(广告类型)，budget(推广预算)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitRequest {
    private Long planId;
    private String unitName;
    /**标识广告位的类型，开屏/贴片*/
    private Integer positionType;
    /**推广单元预算*/
    private Long budget;


    /**校验推广单元是否为null
     * 4种传参，每一种都不为null即整体不为null
     * 计划Id不为null，推广单元名称不为null，广告位类型，预算不为null
     * */
    public boolean createValidate(){
        return null != planId
                && !StringUtils.isEmpty(unitName)
                && positionType !=null
                && budget !=null;
    }
}
