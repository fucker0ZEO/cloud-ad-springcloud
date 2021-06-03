package com.cs.ad.service.impl;

import com.cs.ad.constant.CommonStatus;
import com.cs.ad.constant.Constants;
import com.cs.ad.dao.AdPlanRepository;
import com.cs.ad.dao.AdUserRepository;
import com.cs.ad.entity.AdPlan;
import com.cs.ad.entity.AdUser;
import com.cs.ad.exception.AdException;
import com.cs.ad.service.IAdPlanService;
import com.cs.ad.util.CommonUtils;
import com.cs.ad.vo.request.AdPlanGetRequest;
import com.cs.ad.vo.request.AdPlanRequest;
import com.cs.ad.vo.response.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author fucker
 * IAdPlanService的实现类，对推广计划进行CRUD
 */
@Service
@Slf4j
public class AdPlanServiceImpl implements IAdPlanService {

    /**注入user的DAO接口和plan的DAO接口*/
    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;
    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
//        写入DB之前先判空,校验request是否有值
        if(!request.createValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        再次查询SQL校验和plan关联的用户是否存在
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
//      adUser是否为null，为null则返回 找不到记录
        if (!adUser.isPresent()){
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
//       查询SQL校验DB中的adPlan是否已经存在
        Optional<AdPlan> oldPlan = planRepository.findById(request.getId());
//        如果adPlan已存在
        if (oldPlan !=null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }

//        最后写入数据，创建推广计划
        AdPlan newPlan = planRepository.save(
//                AdPlan实体类中自定义的构造函数中需要写入userId,PlanName,StartDate,EndDate4个值
                new AdPlan(request.getUserId(), request.getPlanName(),
//                        转化时间日期格式，再写入DB
                        CommonUtils.parseStringDate(request.getStartDate()),
                        CommonUtils.parseStringDate(request.getEndDate()))
        );
//        DB中查询的数据写会回Response中，需要返回的id，planName2个数据
        return new AdPlanResponse(newPlan.getId(), newPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
//        校验request是否为null，避免无效查询
        if (!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        根据Ids和userId查询DB，返回对应的数据给对应的response
        return planRepository.findAllByIdInAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        //        写入DB之前先判空,校验request是否有值
        if (!request.updateValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
//        根据planID和userID查询SQL校验plan是否已经存在，存在才能进行数据更新
        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
//        plan为null即查不到推广计划，找不到数据记录
        if (plan == null) {
         throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }


      /*为了确保更新入DB的数据不为null，
       * 需要进行单项更新plan对象内的值，
       * 最后整体将plan对象写回到DB*/

//        前端传入的planName如果不为null，则写入plan对象中
        if (request.getPlanName() !=null){
            plan.setPlanName(request.getPlanName());
        }
        //前端传入的StartDate如果不为null，则转化日期格式后写入plan对象中
        if (request.getStartDate() !=null){
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        //前端传入的StartDate如果不为null，则转化日期格式后写入plan对象中
        if (request.getEndDate() != null) {
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }
//        将当前时间写入plan对象更新时间中
        plan.setUpdateTime(new Date());

//        使用save方法整体写入DB中
        AdPlan newAdPlan = planRepository.save(plan);

//        向AdPlanResponse中写入id和planName，并返回前端
        return new AdPlanResponse(newAdPlan.getId(), newAdPlan.getPlanName());
    }

    @Override
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        //        写入DB之前先判空,校验request是否有值
        if (!request.deleteValidate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        //根据planID和userID查询SQL校验plan是否已经存在，存在才能进行删除
        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        //plan为null即查不到推广计划，找不到数据记录。即无法进行删除
        if (plan == null) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }
        /*看似是删除，实际上DB中的数据应该减少删除!
        因此用更新代替删除！
        将推广计划的状态置为无效，
        同时更新updateTime
        每一次更新都需要更新updateTime为现在的时间*/
        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());

//        save方法将plan更新回DB
        planRepository.save(plan);

    }
}
