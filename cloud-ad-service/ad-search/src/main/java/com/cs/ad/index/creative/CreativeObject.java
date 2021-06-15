package com.cs.ad.index.creative;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeObject {
    private  Long adId;
    private String name;
    private Integer type;
    /**广告素材的类型，例如图像是gif格式还是JPG格式*/
    private Integer materialType;
    private Integer height;
    private Integer width;
    /**审核状态*/
    private Integer auditStatus;
    private String adUrl;



    /**依次校验，单值更新
     * 为null就不更新*/
    public void update(CreativeObject newObject){
        if (null != newObject.getAdId()) {
            this.adId = newObject.getAdId();
        }
        if (null != newObject.getName()) {
            this.name = newObject.getName();
        }
        if (null != newObject.getType()) {
            this.type = newObject.getType();
        }
        if (null != newObject.getMaterialType()) {
            this.materialType = newObject.getMaterialType();
        }
        if (null != newObject.getHeight()) {
            this.height = newObject.getHeight();
        }
        if (null != newObject.getWidth()) {
            this.width = newObject.getWidth();
        }
        if (null != newObject.getAuditStatus()) {
            this.auditStatus = newObject.getAuditStatus();
        }
        if (null != newObject.getAdUrl()) {
            this.adUrl = newObject.getAdUrl();
        }
    }

}
