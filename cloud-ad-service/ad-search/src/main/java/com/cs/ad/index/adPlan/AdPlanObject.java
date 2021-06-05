package com.cs.ad.index.adPlan;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author fucker
 * AdPlan索引的实体类对象--每一张表的索引都会对应一个实体对象
 * 不是表中的所有字段都要建索引，只建能够加速检索的字段
 * 例如planName就不需要建立索引
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;

    /**更新索引时的方法，
     * 通过校验传入的每个值是否为null，
     * 来完成单值的依次更新*/
    public void update(AdPlanObject newAdPlanObject){
//        校验planId是否为null
        if (newAdPlanObject.getPlanId() !=null){
//            将传入的id赋值给原本的planId完成更新
            this.planId = newAdPlanObject.getPlanId();
        }
        //校验userId是否为null
        if (newAdPlanObject.getUserId() !=null){
            //将传入的id赋值给原本的userId完成更新
            this.userId = newAdPlanObject.getUserId();
        }
        //校验planStatus是否为null
        if (newAdPlanObject.getPlanStatus() !=null){
            //将传入的planStatus赋值给原本的planStatus完成更新
            this.planStatus = newAdPlanObject.getPlanStatus();
        }
        //校验startDate是否为null
        if (newAdPlanObject.getStartDate() !=null){
            //将传入的startDate赋值给原本的startDate完成更新
            this.startDate = newAdPlanObject.getStartDate();
        }
        //校验endDate是否为null
        if (newAdPlanObject.getEndDate() != null){
            //将传入的endDate赋值给原本的endDate完成更新
            this.endDate = newAdPlanObject.getEndDate();
        }
    }
}
