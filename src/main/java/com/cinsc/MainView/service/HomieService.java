package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.NoticeDto;
import com.cinsc.MainView.vo.ResultVo;
import org.apache.catalina.LifecycleState;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午4:10
 * @Description:
 */
public interface HomieService {


    /*好友搜寻*/
    ResultVo findHomie(String userAccount);

    /*发送好友验证信息*/
    ResultVo sendCheckMessage(NoticeDto noticeDto,HttpServletRequest request);

    /*好友验证信息确认*/
    ResultVo confirmCheckMessage(String id, String userName,HttpServletRequest request);

    /*改变消息状态*/
    ResultVo readMessage(String id);

    /*删除消息*/
    ResultVo deleteMessage(String id, HttpServletRequest request);

    /*删除好友*/
    ResultVo deleteFriend(Integer userId, HttpServletRequest request);

    /*发送消息*/
    ResultVo sendMessage(Integer userId, String content, HttpServletRequest request);

    /*群发消息*/
    ResultVo massTexting(List<Integer> userIdList, String content, HttpServletRequest request);

    /*获得好友验证消息*/
    ResultVo getCheckMessage(HttpServletRequest request);

    /*得到自己未读的消息*/
    ResultVo getUnreadMessage(HttpServletRequest request);

    /*得到自己已读的消息*/
    ResultVo getReadedMessage(HttpServletRequest request);

    /*好友名片显示*/
    ResultVo getFriendCards(Integer userId);

    /*获取好友列表*/
    ResultVo getFriendsList(HttpServletRequest request);

    /*判断是否是好友*/
    ResultVo isFriend(String userAccount,HttpServletRequest request);

    /*判断是否有未读消息*/
    ResultVo isUnreadMessage(HttpServletRequest request);

}
