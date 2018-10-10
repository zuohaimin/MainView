package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.UserRegistDto;
import com.cinsc.MainView.vo.ResultVo;

/**
 * @Author: 束手就擒
 * @Date: 18-8-4 下午12:55
 * @Description:
 */
public interface UserRegist {

    ResultVo ifNotExitsUserAccount(String userAccount);

    ResultVo login(String password, String userAccount);

    ResultVo ifExitsUserAccount(String userAccount);

    /*发送验证码*/
    ResultVo sendSMS(String userAccount);

    /*检查验证码*/
    ResultVo checkSMS(String sms, String userAccount);

//    /*保存用户信息*/
//    ResultVo saveUserAccount(String password, String userAccount);

    /*更新用户信息*/
    ResultVo updateUserAccount(UserRegistDto userRegistDto);

    /*注册新用户*/
    ResultVo regist(UserRegistDto userRegistDto);


}
