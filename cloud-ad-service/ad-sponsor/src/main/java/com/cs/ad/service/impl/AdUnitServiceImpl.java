package com.cs.ad.service.impl;

import com.cs.ad.constant.Constants;
import com.cs.ad.dao.AdPlanRepository;
import com.cs.ad.dao.AdUnitRepository;
import com.cs.ad.dao.CreativeRepository;
import com.cs.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.cs.ad.dao.unit_condition.AdUnitItRepository;
import com.cs.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.cs.ad.dao.unit_condition.CreativeUnitRepository;
import com.cs.ad.entity.AdPlan;
import com.cs.ad.entity.AdUnit;
import com.cs.ad.entity.unit_condition.AdUnitDistrict;
import com.cs.ad.entity.unit_condition.AdUnitIt;
import com.cs.ad.entity.unit_condition.AdUnitKeyword;
import com.cs.ad.entity.unit_condition.CreativeUnit;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IAdUnitService;
import com.cs.ad.vo.request.*;
import com.cs.ad.vo.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fucker
 * IAdUnitService的实现类，对推广单元进行CRUD
 */
@Service
@Slf4j
public class AdUnitServiceImpl implements IAdUnitService {

    /**注入除User外所有的DAO接口*/
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;
    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository planRepository,
                             AdUnitRepository unitRepository,
                             AdUnitKeywordRepository unitKeywordRepository,
                             AdUnitItRepository unitItRepository, AdUnitDistrictRepository unitDistrictRepository,
                             CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository){
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    @Transactional
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        //写入数据前，先校验Request是否为null
        if (!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        //查询SQL校验关联的AdPlan是否已存在，若对应的推广计划不存在，则不能写入.返回找不到数据记录
        Optional<AdPlan> adPlan = planRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        //查询SQL校验推广单元是否存在，推广单元若存在，则不能重复创建推广单元
        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (oldAdUnit != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }

//        校验合格，可以通过save方法写入DB。
        AdUnit newAdUnit = unitRepository.save(
//                自定义的AdUnit实体类中需要写入的planId,unitName,positionType，budget4个参数
                new AdUnit(request.getPlanId(),
                        request.getUnitName(),
                        request.getPositionType(),
                        request.getBudget()));

        //        DB中查询的数据写会回Response中，需要返回的id，unitName2个数据
        return new AdUnitResponse(newAdUnit.getId(),newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();

        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {

            request.getUnitKeywords().forEach(i -> unitKeywords.add(
                    new AdUnitKeyword(i.getUnitId(), i.getKeyword())
            ));
            ids = unitKeywordRepository.saveAll(unitKeywords).stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }

        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitIt> unitIts = new ArrayList<>();
        request.getUnitIts().forEach(i -> unitIts.add(
                new AdUnitIt(i.getUnitId(), i.getItTag())
        ));
        List<Long> ids = unitItRepository.saveAll(unitIts).stream()
                .map(AdUnitIt::getId)
                .collect(Collectors.toList());

        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        request.getUnitDistricts().forEach(d -> unitDistricts.add(
                new AdUnitDistrict(d.getUnitId(), d.getProvince(),
                        d.getCity())
        ));
        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());

        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
            List<Long> unitIds = request.getUnitItems().stream()
                    .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                    .collect(Collectors.toList());
            List<Long> creativeIds = request.getUnitItems().stream()
                    .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                    .collect(Collectors.toList());

            if (!(isRelatedUnitExist(unitIds) && isRelatedCreativeExist(creativeIds))) {
                throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
            }

            List<CreativeUnit> creativeUnits = new ArrayList<>();
            request.getUnitItems().forEach(i -> creativeUnits.add(
                    new CreativeUnit(i.getCreativeId(), i.getUnitId())
            ));

            List<Long> ids = creativeUnitRepository.saveAll(creativeUnits)
                    .stream()
                    .map(CreativeUnit::getId)
                    .collect(Collectors.toList());

            return new CreativeUnitResponse(ids);
    }

    /**判断推广单元是否存在
     * */
    private boolean isRelatedUnitExist(List<Long> unitIds) {

        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        return unitRepository.findAllById(unitIds).size() ==
                new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds) {

        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        return creativeRepository.findAllById(creativeIds).size() ==
                new HashSet<>(creativeIds).size();
    }
}
