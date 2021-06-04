package com.cs.ad.service;

import com.cs.ad.vo.request.CreativeRequest;
import com.cs.ad.vo.response.CreativeResponse;

/**
 * @author fucker
 * 创意表的CRUD
 */
public interface ICreativeService {
    /**
     *
     *createCreative
     * @description: 创建创意
     * @param request 创意的前端请求对象
     * @return CreativeResponse
     */
    CreativeResponse createCreative(CreativeRequest request);
}
