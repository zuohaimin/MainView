package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.dto.ArrangeDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.enums.TransactorStatusEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.model.*;
import com.cinsc.MainView.repository.*;
import com.cinsc.MainView.service.ArrangeService;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.key.KeyUtil;
import com.cinsc.MainView.vo.ArrangeVo;
import com.cinsc.MainView.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: 束手就擒
 * @Date: 18-8-14 下午8:07
 * @Description:
 */
@Service
@Slf4j
public class ArrangeServiceImpl implements ArrangeService {

    private ArrangeRepository arrangeRepository;
    private UserLoginRepository userLoginRepository;
    private UserDetailRepository userDetailRepository;
    private TransactorRepository transactorRepository;
    private CcRepository ccRepository;

    @Autowired
    public ArrangeServiceImpl(ArrangeRepository arrangeRepository,
                              UserLoginRepository userLoginRepository,
                              UserDetailRepository userDetailRepository,
                              TransactorRepository transactorRepository,
                              CcRepository ccRepository) {
        this.arrangeRepository = arrangeRepository;
        this.userLoginRepository = userLoginRepository;
        this.userDetailRepository = userDetailRepository;
        this.transactorRepository = transactorRepository;
        this.ccRepository = ccRepository;
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
        arrangeList.forEach(o -> {
            ArrangeVo arrangeVo = new ArrangeVo();
            arrangeVo.setUserName(getUsername(o.getAuthor()));
            arrangeVo.setDeadLine(o.getDeadLine());
            arrangeVo.setDescription(o.getDescription());
            arrangeVo.setTotalNum(o.getTotalNum());
            arrangeVo.setFinishNum(o.getFinishNum());
            arrangeVoList.add(arrangeVo);
        });
        return arrangeVoList;
    }

    private Set<String> getArrangeIdList(TransactorStatusEnum statusEnum, HttpServletRequest request) {
        List<Transactor> transactorList = transactorRepository.findByUserId(ShiroUtil.getUserId(request));
        Set<String> arrangeIdSet = new HashSet<>();
        transactorList.forEach(o -> {
            if (o.getStatus().equals(statusEnum.getCode())) {
                arrangeIdSet.add(o.getArrangeId());
            }
        });
        return arrangeIdSet;
    }

    @Override
    @Transactional
    public ResultVo addArrange(ArrangeDto arrangeDto, HttpServletRequest request) {
        Arrange arrange = new Arrange();
        String arrangeId = KeyUtil.genUniqueKey();
        arrange.setArrangeId(arrangeId);
        arrange.setAuthor(ShiroUtil.getUserId(request));
        Date now = new Date();
        arrange.setCreateTime(now);
        arrange.setDeadLine(calculateDate(arrangeDto.getDays(),now));
        arrange.setDescription(arrangeDto.getMsg());
        arrange.setTotalNum(arrangeDto.getTransactorIdList().size());
        Arrange arrangeSave = arrangeRepository.save(arrange);
        log.info("添加工作安排 arrangeSave = {}", arrangeSave);

        List<Transactor> transactorList = new ArrayList<>();
        arrangeDto.getTransactorIdList().forEach(o -> {
            Transactor transactor = new Transactor();
            transactor.setArrangeId(arrangeId);
            transactor.setUserId(o);
            transactorList.add(transactor);
        });
        List<Transactor> transactorListSave = transactorRepository.saveAll(transactorList);
        log.info("添加执行人集 transactorListSave = {}", transactorListSave);

//        List<Cc> ccList = new ArrayList<>();
//        arrangeDto.getCarbonCopyIdList().forEach(o -> {
//            Cc cc = new Cc();
//            cc.setArrangeId(arrangeId);
//            cc.setUserId(o);
//            ccList.add(cc);
//        });
//        List<Cc> ccListSave = ccRepository.saveAll(ccList);
//        log.info("添加抄送人集 ccListSave = {}", ccListSave);
        return ResultVoUtil.success();
    }

    @Override
    @Transactional
    public ResultVo finishArrange(String arrangeId, HttpServletRequest request) {
        /*执行表更新*/
        Transactor transactor = transactorRepository.findByArrangeIdAndUserId(arrangeId,ShiroUtil.getUserId(request));
        if (transactor == null){
            log.info("完成安排 没有找到指定条件的执行表 transactor = null");
            throw new SystemException(ResultEnum.DATA_ERROR);
        }
        /*找到指定安排*/
        Arrange arrange = arrangeRepository.findById(arrangeId).orElseThrow(()-> new SystemException(ResultEnum.DATA_ERROR));
        Integer finishNum = arrange.getFinishNum();
        if (TransactorStatusEnum.READY.getCode().equals(transactor.getStatus())){
            transactor.setStatus(TransactorStatusEnum.FINISHED.getCode());
            Transactor transactorSave = transactorRepository.save(transactor);
            log.info("完成安排 transactorSave = {}",transactorSave);
            arrange.setFinishNum(finishNum+1);
            Arrange arrangeSave = arrangeRepository.save(arrange);
            log.info("完成安排 arrangeSave = {}",arrangeSave);

        }else{
            transactor.setStatus(TransactorStatusEnum.READY.getCode());
            Transactor transactorSave = transactorRepository.save(transactor);
            log.info("完成安排 transactorSave = {}",transactorSave);
            arrange.setFinishNum(finishNum-1);
            Arrange arrangeSave = arrangeRepository.save(arrange);
            log.info("完成安排 arrangeSave = {}",arrangeSave);
        }


        /*安排表更新*/
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getUnfinishedArrange(HttpServletRequest request) {
        Set<String> arrangeIdSet = getArrangeIdList(TransactorStatusEnum.READY, request);
        List<Arrange> arrangeList = arrangeRepository.findAllById(arrangeIdSet);
        log.info("获取未完成安排 arrangeList = {}", arrangeList);

        return ResultVoUtil.success(getArrangeVoList(arrangeList));
    }

    @Override
    public ResultVo getFinishedArrange(HttpServletRequest request) {
        Set<String> arrangeIdSet = getArrangeIdList(TransactorStatusEnum.FINISHED, request);
        List<Arrange> arrangeList = arrangeRepository.findAllById(arrangeIdSet);
        log.info("获取已经完成安排 arrangeList = {}", arrangeList);
        return ResultVoUtil.success(getArrangeVoList(arrangeList));
    }

    @Override
    public ResultVo getSendedArrange(HttpServletRequest request) {
        List<Arrange> arrangeList = arrangeRepository.findByAuthor(ShiroUtil.getUserId(request));
        return ResultVoUtil.success(getArrangeVoList(arrangeList));
    }

    @Override
    public ResultVo getDoneArrange(HttpServletRequest request) {
        List<Transactor> transactorList = transactorRepository.findByUserId(ShiroUtil.getUserId(request));
        Set<String> arrangeIdSet = new HashSet<>();
        transactorList.forEach(o ->
                arrangeIdSet.add(o.getArrangeId())
        );
        List<Arrange> arrangeList = arrangeRepository.findAllById(arrangeIdSet);
        return ResultVoUtil.success(getArrangeVoList(arrangeList));
    }

    @Override
    public ResultVo getCCArrange(HttpServletRequest request) {
        List<Cc> ccList = ccRepository.findByUserId(ShiroUtil.getUserId(request));
        Set<String> arrangeIdSet = new HashSet<>();
        ccList.forEach(o ->
                arrangeIdSet.add(o.getArrangeId())
        );
        List<Arrange> arrangeList = arrangeRepository.findAllById(arrangeIdSet);
        return ResultVoUtil.success(getArrangeVoList(arrangeList));
    }
}
