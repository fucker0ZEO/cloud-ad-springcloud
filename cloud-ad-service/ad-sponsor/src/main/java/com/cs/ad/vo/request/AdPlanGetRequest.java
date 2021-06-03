package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author fucker
 * 接受前端(通过get方法)传过来推广计划的请求对象
 * 需要接受的数据：ids(即多个planId)，userId
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanGetRequest {

    private Long userId;
    private List<Long> ids;

    /**校验userId，和推广计划id列表（ids）不为null*/
    public boolean validate(){
        return userId !=null
                && !CollectionUtils.isEmpty(ids);

    }
}
