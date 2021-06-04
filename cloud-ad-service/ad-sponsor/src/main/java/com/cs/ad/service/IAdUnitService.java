package com.cs.ad.service;

import com.cs.ad.exception.AdException;
import com.cs.ad.vo.request.*;
import com.cs.ad.vo.response.*;

/**
 * @author fucker
 * 对AdUnit进行CRUD
 */
public interface IAdUnitService {

    /**
     *
     *createUnit
     * @description: 创建推广单元
     * @param request 前端传入的推广单元请求对象
     * @return AdUnitResponse 返回给前端的推广单元响应对象
     * @throws AdException
     */
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    /**创建推广单元，限制维度为关键词*/
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request)
            throws AdException;

    /**创建推广单元，限制维度为爱好*/
    AdUnitItResponse createUnitIt(AdUnitItRequest request)
            throws AdException;

    /**创建推广单元，限制维度为地域*/
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request)
            throws AdException;


    /**
     *createCreativeUnit
     * @description: 创意推广单元创意
     * 向creative_unit表中插入数据
     * 表中有3个字段：主键id，creativeId，unitId3个字段
     * 主键id自动生成，接受的请求对象中有另外2个id，无需额外的处理即可插入数据
     * @param request 返回的响应对象--返回的数据为主键ids
     * @return CreativeUnitResponse 接受的请求对象
     * @throws AdException 广告系统统一异常处理
     */
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request)
            throws AdException;

}
