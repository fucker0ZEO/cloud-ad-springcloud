package com.cs.ad.service.impl;

import com.cs.ad.dao.CreativeRepository;
import com.cs.ad.entity.Creative;
import com.cs.ad.service.ICreativeService;
import com.cs.ad.vo.request.CreativeRequest;
import com.cs.ad.vo.response.CreativeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fucker
 */
@Service
public class CreativeServiceImpl implements ICreativeService {

    @Autowired
    private CreativeRepository creativeRepository;

    @Override
    public CreativeResponse createCreative(CreativeRequest request) {

//        creative的DAO接口的save方法实现保存
        Creative creative = creativeRepository.save(
//                调用转化方法，将接收的请求对象转化为实体类，需要补全的全部补全
                request.convertToEntity()
        );
//        返回id和name
        return new CreativeResponse(creative.getId(),creative.getName());
    }
}
