package com.cs.ad.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 * Creative和推广单元相关联的接收的请求对象
 * 接受的数据：创意id，推广单元id
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreativeUnitItem{
        private Long creativeId;
        private Long unitId;
    }
    /**实现批量创建*/
    private List<CreativeUnitItem> unitItems;
}
