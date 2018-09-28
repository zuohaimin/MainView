package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.NoticeDto;
import com.cinsc.MainView.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;

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
    ResultVo confirmCheckMessage(String id, String friendAccount,HttpServletRequest request);

    /*置消息已读*/
    ResultVo readMessage(String id);

    /*置消息未读*/
    ResultVo unreadMessage(String id);

    /*删除好友*/
    ResultVo deleteFriend(Integer userId, HttpServletRequest request);

    /*发送消息*/
    ResultVo sendMessage(Integer userId, String content, HttpServletRequest request);

    /*得到消息*/
    ResultVo getOwnerMessage(HttpServletRequest request);

    /*得到自己未读的消息*/
    ResultVo getUnreadMessage(HttpServletRequest request);

    /*得到自己已读的消息*/
    ResultVo getReadedMessage(HttpServletRequest request);

    /*好友名片显示*/
    ResultVo getFriendCards(Integer userId);

    /*获取好友列表*/
    ResultVo getFriendsList(HttpServletRequest request);

    /*shifouhaoyou*/
    ResultVo isFriend(String userAccount,HttpServletRequest request);


}
