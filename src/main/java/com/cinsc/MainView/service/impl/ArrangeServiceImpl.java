package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.dto.ArrangeDto;
import com.cinsc.MainView.enums.ArrangeStatusEnum;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.enums.TransactorStatusEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.model.*;
import com.cinsc.MainView.repository.*;
import com.cinsc.MainView.service.ArrangeService;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.TimeUtil;
import com.cinsc.MainView.utils.convert.MapTurnPojo;
import com.cinsc.MainView.utils.convert.PictureToBase64;
import com.cinsc.MainView.utils.key.KeyUtil;
import com.cinsc.MainView.vo.ArrangeScheduleVo;
import com.cinsc.MainView.vo.ArrangeVo;
import com.cinsc.MainView.vo.ResultVo;
import com.cinsc.MainView.vo.UserMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 束手就擒
 * @Date: 18-8-14 下午8:07
 * @Description:
 */
@Service
@Slf4j
public class ArrangeServiceImpl implements ArrangeService {

    private ArrangeRepository arrangeRepository;
    private UserDetailRepository userDetailRepository;
    private TransactorRepository transactorRepository;

    @Autowired
    public ArrangeServiceImpl(ArrangeRepository arrangeRepository,
                              UserDetailRepository userDetailRepository,
                              TransactorRepository transactorRepository) {
        this.arrangeRepository = arrangeRepository;
        this.userDetailRepository = userDetailRepository;
        this.transactorRepository = transactorRepository;
    }


    /**
     * 获取用户名
     *
     * @param userId
     * @return
     */
    private String getUsername(Integer userId) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        log.info("获取用户名 UserDetail={}", userDetail);
        if (userDetail == null) {
            log.info("[获取用户名] userDetail == null");
            throw new SystemException(ResultEnum.UNkNOWN_ACCOUNT);
        }
        return userDetail.getUserName();
    }

    private Date calculateDate(Integer days, Date now){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.DATE,+days);
        return calendar.getTime();
    }
    /**
     * 装配ArrangeVo
     *
     * @param arrangeList
     * @return
     */
    private List<ArrangeVo> getArrangeVoList(List<Arrange> arrangeList) {
        List<ArrangeVo> arrangeVoList = new ArrayList<>();
        arrangeList.forEach(o -> arrangeVoList.add(getArrangeVo(o)));
        return arrangeVoList;
    }

    private Integer getUserStatus(List<Transactor> transactorList,Integer userId){
        for (Transactor transactor : transactorList) {
            if (transactor.getUserId().equals(userId)){
                return transactor.getStatus();
            }
        }
        throw new SystemException(ResultEnum.DATA_ERROR);
    }
    private ArrangeVo getArrangeVo(Arrange arrange) {
        ArrangeVo arrangeVo = new ArrangeVo();
        BeanUtils.copyProperties(arrange,arrangeVo);
        arrangeVo.setCreateTime(arrange.getCreateTime().getTime());
        arrangeVo.setDeadLine(arrange.getDeadLine().getTime());
        arrangeVo.setUserName(getUsername(arrange.getAuthor()));
        return arrangeVo;
    }

    private List<Transactor> mergeCollection(List<Transactor> transReadyList, List<Transactor> transFinishList){
        List<Transactor> transactorList;
        if (null == transReadyList & null == transFinishList){
            log.info("[获得我执行的安排] transReadyList transFinishList == null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        if (transReadyList != null){
            transactorList = new ArrayList<>(transReadyList);
            transactorList.addAll(transFinishList);
            return transactorList;
        }else{
            return new ArrayList<>(transFinishList);
        }
    }

    private List<Arrange> getWorkArrange(List<Arrange> arrangeList){
        return  arrangeList.stream().filter(o->!MainViewConstant.DEFAULT_SCHEDULE_TOTALNUM.equals(o.getTotalNum())).collect(Collectors.toList());
    }
    private List<Arrange> getScheduleArrange(List<Arrange> arrangeList){
        return  arrangeList.stream().filter(o->MainViewConstant.DEFAULT_SCHEDULE_TOTALNUM.equals(o.getTotalNum())).collect(Collectors.toList());
    }
    private Set<Integer> removalRepeat(List<Integer> transactorIdList){
        return new HashSet<>(transactorIdList);

    }




    private Arrange getArrange(String arrangeId, Date now, Date deadLine, String description, Integer totalNum, HttpServletRequest request){
        Arrange arrange = new Arrange();
        arrange.setArrangeId(arrangeId);
        arrange.setAuthor(ShiroUtil.getUserId(request));
        arrange.setCreateTime(now);
        arrange.setDeadLine(deadLine);
        arrange.setDescription(description);
        arrange.setTotalNum(totalNum);
        return arrange;
    }
    @Override
    @Transactional
    public ResultVo addArrange(ArrangeDto arrangeDto, HttpServletRequest request) {
        log.info("[添加安排] arrangeDto={}",arrangeDto);
        Date now = new Date();
        Date deadline = calculateDate(arrangeDto.getDays(),now);
        String arrangeId = KeyUtil.genUniqueKey();
        Set<Integer> transactorSet = removalRepeat(arrangeDto.getTransactorIdList());
        Arrange arrange = getArrange(arrangeId,now,deadline,arrangeDto.getMsg(),transactorSet.size(),request);
        Arrange arrangeSave = arrangeRepository.save(arrange);
        log.info("添加工作安排 arrangeSave = {}", arrangeSave);

        List<Transactor> transactorList = new ArrayList<>();
        transactorSet.forEach(o -> {
            Transactor transactor = new Transactor();
            transactor.setArrangeId(arrangeId);
            transactor.setUserId(o);
            transactorList.add(transactor);
        });
        List<Transactor> transactorListSave = transactorRepository.saveAll(transactorList);
        log.info("添加执行人集 transactorListSave = {}", transactorListSave);
        return ResultVoUtil.success();
    }

    @Override
    @Transactional
    //2018.10.10 解决同一个用户多次完成安排的bug
    public ResultVo finishArrange(String arrangeId, HttpServletRequest request) {
        /*判断是否是非法操作*/
        /*执行表更新*/
        Transactor transactor = transactorRepository.findByArrangeIdAndUserId(arrangeId,ShiroUtil.getUserId(request));
        if (transactor == null){
            log.info("[完成工作安排] 没有找到指定条件的执行表(非法操作) transactor = null");
            throw new SystemException(ResultEnum.ILLEGAL_OPERATION);
        }
        /*找到指定安排*/
        Arrange arrange = arrangeRepository.findById(arrangeId).orElseThrow(()-> new SystemException(ResultEnum.DATA_ERROR));
        if (TransactorStatusEnum.READY.getCode().equals(transactor.getStatus())){
            transactor.setStatus(TransactorStatusEnum.FINISHED.getCode());
            Transactor transactorSave = transactorRepository.save(transactor);
            log.info("[完成工作安排] transactorSave = {}",transactorSave);

        }else{
            transactor.setStatus(TransactorStatusEnum.READY.getCode());
            Transactor transactorSave = transactorRepository.save(transactor);
            log.info("[完成工作安排] transactorSave = {}",transactorSave);
        }

        /*Arrange finish数量的增减 */

        List<Transactor> transactorList = transactorRepository.findByArrangeId(arrangeId);
        if (null == transactorList){
            log.info("[完成工作安排] transactorList == null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        Integer count = (int)transactorList.stream().filter(o-> TransactorStatusEnum.FINISHED.getCode().equals(o.getStatus()))
                               .count();
        arrange.setFinishNum(count);
        /*安排表更新*/
        if (count.equals(arrange.getTotalNum())){
            arrange.setStatus(ArrangeStatusEnum.FINISH.getCode());
            log.info("[完成工作安排] arrange={}已经完成",arrange);
        }else{
            arrange.setStatus(ArrangeStatusEnum.WAIT.getCode());
            log.info("[完成工作安排] arrange={}未完成",arrange);
        }
        Arrange arrangeSave = arrangeRepository.save(arrange);
        log.info("完成安排 arrangeSave = {}",arrangeSave);


        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getUnfinishedArrange(HttpServletRequest request) {
        List<Arrange> arrangeList = arrangeRepository.findByStatus(ArrangeStatusEnum.WAIT.getCode());
        if (null == arrangeList){
            log.info("[获得未完成安排] arrangeList = null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        List<Arrange> arrangeWorkList = getWorkArrange(arrangeList);
        return ResultVoUtil.success(getArrangeVoList(arrangeWorkList));
    }

    @Override
    public ResultVo getFinishedArrange(HttpServletRequest request) {
        List<Arrange> arrangeList = arrangeRepository.findByStatus(ArrangeStatusEnum.FINISH.getCode());
        if (null == arrangeList){
            log.info("[获得已完成安排] arrangeList = null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        List<Arrange> arrangeWorkList = getWorkArrange(arrangeList);
        return ResultVoUtil.success(getArrangeVoList(arrangeWorkList));
    }

    @Override
    public ResultVo getSendedArrange(HttpServletRequest request) {
        List<Arrange> arrangeList = arrangeRepository.findByAuthor(ShiroUtil.getUserId(request));
        List<Arrange> arrangeWorkList = getWorkArrange(arrangeList);
        return ResultVoUtil.success(getArrangeVoList(arrangeWorkList));
    }

    //2018.10.11 添加status
    //2018.10.12 status有序
    //TODO N+1
    @Override
    public ResultVo getDoneArrange(HttpServletRequest request) {
        Integer userId = ShiroUtil.getUserId(request);
        List<Transactor> transReadyList = transactorRepository.findByUserIdAndStatus(userId,TransactorStatusEnum.READY.getCode());
        List<Transactor> transFinishList = transactorRepository.findByUserIdAndStatus(userId,TransactorStatusEnum.FINISHED.getCode());

        List<Transactor> transactorList = mergeCollection(transReadyList,transFinishList);

        List<Map<String,Object>> arrangeVoMapList = new ArrayList<>();
        transactorList.forEach(o->{
                ArrangeVo arrangeVo =getArrangeVo(arrangeRepository.findById(o.getArrangeId()).orElseThrow(()->new SystemException(ResultEnum.NOT_FOUND)));
                /*查找我完成的情况*/
                Map<String,Object> arrangeVoMap = MapTurnPojo.object2Map(arrangeVo);
                arrangeVoMap.put("status",o.getStatus());
                if (!MainViewConstant.DEFAULT_SCHEDULE_TOTALNUM.equals(arrangeVo.getTotalNum())){
                    arrangeVoMapList.add(arrangeVoMap);
                }
        });
        /*查找我完成的情况*/

        return ResultVoUtil.success(arrangeVoMapList);
    }


    @Override
    public ResultVo addScheduleArrange(Date createTime, Date deadLine, String description, HttpServletRequest request) {

        Arrange arrange = getArrange(KeyUtil.genUniqueKey(),createTime,deadLine,description, MainViewConstant.DEFAULT_SCHEDULE_TOTALNUM,request);
        Arrange arrangeSave = arrangeRepository.save(arrange);
        log.info("[添加日程安排] arrangeSave = {}",arrangeSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo finishScheduleArrange(String arrangeId, HttpServletRequest request) {
        /*设置 status为 完成状态*/
        Arrange arrange = arrangeRepository.findById(arrangeId).orElseThrow(()-> new SystemException(ResultEnum.NOT_FOUND));
        if (!arrange.getAuthor().equals(ShiroUtil.getUserId(request))){
            log.info("[完成日程安排] 非法操作");
            throw new SystemException(ResultEnum.ILLEGAL_OPERATION);
        }
        if (ArrangeStatusEnum.WAIT.getCode().equals(arrange.getStatus())){
            arrange.setStatus(ArrangeStatusEnum.FINISH.getCode());
        }else{
            arrange.setStatus(ArrangeStatusEnum.WAIT.getCode());
        }
        Arrange arrangeSave = arrangeRepository.save(arrange);
        log.info("[完成日程安排] 保存arrangeSave={}",arrangeSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getScheduleArrange(long time, HttpServletRequest request) {
        List<Arrange> arrangeList = arrangeRepository.findByAuthor(ShiroUtil.getUserId(request));
        List<Arrange> arrangeScheduleList = getScheduleArrange(arrangeList);
        if (null == arrangeScheduleList){
            log.info("[获得日程安排] arrangeScheduleList == null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        List<ArrangeScheduleVo> arrangeScheduleVoList = new ArrayList<>();
        Date inputDate = new Date(time);
        arrangeScheduleList.stream().filter(o-> TimeUtil.isSameDate(inputDate,o.getCreateTime()))
                                    .forEach(o->{
                                        ArrangeScheduleVo arrangeScheduleVo = new ArrangeScheduleVo();
                                        BeanUtils.copyProperties(o,arrangeScheduleVo);
                                        arrangeScheduleVo.setCreateTime(o.getCreateTime().getTime());
                                        arrangeScheduleVo.setDeadLine(o.getDeadLine().getTime());
                                        arrangeScheduleVoList.add(arrangeScheduleVo);
                                    });
        return ResultVoUtil.success(arrangeScheduleVoList);

    }

    @Override
    @Transactional
    public ResultVo deleteWorkArrange(String arrangeId, HttpServletRequest request) {
        deleteScheduleArrange(arrangeId,request);
        transactorRepository.deleteByArrangeId(arrangeId);
        return ResultVoUtil.success();
    }

    @Override
    @Transactional
    public ResultVo deleteScheduleArrange(String arrangeId, HttpServletRequest request) {
        Arrange arrange = arrangeRepository.findByArrangeIdAndAuthor(arrangeId,ShiroUtil.getUserId(request));
        if (null == arrange){
            log.info("[删除安排] 操作非法");
            throw new SystemException(ResultEnum.ILLEGAL_OPERATION);
        }
        arrangeRepository.delete(arrange);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getArrangeTransactors(String arrangeId) {
        List<Transactor> transactorList = transactorRepository.findByArrangeId(arrangeId);
        if (null == transactorList){
            log.info("得到安排的成员信息");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        List<Integer> userIdList = new ArrayList<>();
        transactorList.forEach(o-> userIdList.add(o.getUserId()));
        List<Map<String,Object>> userMsgMapList = new ArrayList<>();
        /*获得详细信息*/
        userDetailRepository.findByUserIdIn(userIdList).forEach(o->{
            Map<String,Object> userMsgMap = new HashMap<>();
            userMsgMap.put("userName",o.getUserName());

            userMsgMap.put("status",getUserStatus(transactorList,o.getUserId()));
            userMsgMapList.add(userMsgMap);

        });
        return ResultVoUtil.success(userMsgMapList);
    }
}
