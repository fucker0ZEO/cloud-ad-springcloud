package com.cs.ad.index.adUnit;

import com.cs.ad.index.adPlan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucker
 * unitName，budget，更新时间，创建时间
 * 对检索无影响，不用建索引
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Long planId;
    private Integer unitStatus;
    private Integer positionType;

    /**这个也是模型聚合？？？
     * 一个model中嵌套着另一个model
     * 然后只是方便获取推广计划*/
    private AdPlanObject adPlanObject;

    /**同样的update依次增加*/
    void update(AdUnitObject newObject) {

        if (null != newObject.getUnitId()) {
            this.unitId = newObject.getUnitId();
        }
        if (null != newObject.getUnitStatus()) {
            this.unitStatus = newObject.getUnitStatus();
        }
        if (null != newObject.getPositionType()) {
            this.positionType = newObject.getPositionType();
        }
        if (null != planId) {
            this.planId = newObject.getPlanId();
        }
        if (null != newObject.getAdPlanObject()) {
            this.adPlanObject = newObject.getAdPlanObject();
        }
    }

//    使用具体的positionType
}
