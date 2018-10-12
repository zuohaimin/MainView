package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.ArrangeDto;
import com.cinsc.MainView.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午7:10
 * @Description:
 */
public interface ArrangeService {

    /*新建工作安排*/
    ResultVo addArrange(ArrangeDto arrangeDto,HttpServletRequest request);

    /*完成指定工作安排*/
    ResultVo finishArrange(String arrangeId, HttpServletRequest request);

    /*获取未完成的工作安排*/
    ResultVo getUnfinishedArrange(HttpServletRequest request);

    /*获取已经完成的工作安排*/
    ResultVo getFinishedArrange(HttpServletRequest request);

    /*获取我发出的工作安排*/
    ResultVo getSendedArrange(HttpServletRequest request);

    /*获取我执行的工作安排*/
    ResultVo getDoneArrange(HttpServletRequest request);

    /*获取抄送我的工作安排*/
    ResultVo getCCArrange(HttpServletRequest request);

    /*新建日程安排*/
    ResultVo addScheduleArrange(Date deadLine, String description, HttpServletRequest request);

    /*完成指定日程安排*/
    ResultVo finishScheduleArrange(String arrangeId, HttpServletRequest request);

    /*获取指定时间的日程安排*/
    ResultVo getScheduleArrange(Date time, HttpServletRequest request);

    /*删除指定的工作安排*/
    ResultVo deleteWorkArrange(String arrangeId, HttpServletRequest request);

    /*删除指定的日程安排*/
    ResultVo deleteScheduleArrange(String arrangeId, HttpServletRequest request);

    /*获得指定的安排的人员*/
    ResultVo getArrangeTransactors(String arrangeId);
}
