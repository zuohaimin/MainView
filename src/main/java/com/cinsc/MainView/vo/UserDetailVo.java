package com.cinsc.MainView.vo;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.enums.GenderEnum;
import com.cinsc.MainView.enums.MsgStatusEnum;
import com.cinsc.MainView.enums.RoleEnum;
import com.cinsc.MainView.utils.key.KeyUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-8-24 下午7:47
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailVo {

    /*姓名*/
    private String userName;

    /*性别*/
    private Integer userGender = GenderEnum.MALE.getCode();

    /*专业*/
    private String userMajor;

    /*学号*/
    private String studentNumber;

    /*籍贯*/
    private String origin;

    /*教育经历*/
    private Integer education = RoleEnum.UNDERGRADUATE.getCode();

    /*个人简介*/
    private String description;

    /*qq*/
    private String qq;

    /*qq status 默认为隐藏*/
    private Integer qqStatus = MsgStatusEnum.HIDDEN.getCode();


    /*手机号码*/
    private String userPhone;

    /*phone status 默认为隐藏*/
    private Integer phoneStatus = MsgStatusEnum.HIDDEN.getCode();

    /*邮箱*/
    private String userMailbox;

    /*mailbox status 默认为隐藏*/
    private Integer mailboxStatus = MsgStatusEnum.HIDDEN.getCode();

    /*头像*/
    private String userIcon;
}
