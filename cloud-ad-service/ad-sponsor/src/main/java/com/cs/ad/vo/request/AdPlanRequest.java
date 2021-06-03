package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @author fucker
 * 接受前端传过来推广计划的请求对象
 * 需要接受的数据：id，userId，planName，startDate，endDate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {
    private Long id;
    private Long userId;
    private String planName;
    private String startDate;
    private String endDate;

    /**创建推广计划表时校验数据
     * 插入数据，尽量保证每个属性都非null
     * */
    public boolean createValidate(){
        return userId != null
                && !StringUtils.isEmpty(planName)
                && !StringUtils.isEmpty(startDate)
                && !StringUtils.isEmpty(endDate);
    }
    /**校验更新推广计划时是否为null
     * id和userId，即可完全确定一个推广计划
     * （理论上单id也可以，但如果userId为null依旧会bug
     *  毕竟userId对应着user表的主键）
     *  业务上关联多表的写入，主键一定要做非null校验
     * */
    public boolean updateValidate(){
        return id != null
                && userId != null;
    }
    /**删除更新推广计划时是否为null
     * id和userId，即可完全确定一个推广计划
     * （理论上单id也可以，但如果userId为null依旧会bug
     *  毕竟userId对应着user表的主键）
     *  业务上关联多表的写入，主键一定要做非null校验
     * */
    public boolean deleteValidate() {
        return id != null
                && userId != null;
    }
}
