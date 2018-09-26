package com.cinsc.MainView.dto;

import com.cinsc.MainView.enums.GenderEnum;
import com.cinsc.MainView.enums.MsgStatusEnum;
import com.cinsc.MainView.enums.RoleEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;


/**
 * @Author: 束手就擒
 * @Date: 18-8-3 下午8:34
 * @Description:
 */
@Data
public class UserDetailDto {

    /*姓名*/
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "姓名不能为空")
    private String userName;

    /*性别*/
    private Integer userGender;

    /*专业*/
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "专业不能为空")
    private String userMajor;

    /*学号*/
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "学号不能为空")
    private String studentNumber;

    /*籍贯*/
    @NotEmpty(message = "籍贯不能为空")
    private String origin;

    /*教育经历*/
    private String education;

    /*个人简介*/
    @NotEmpty(message = "个人简介不能为空")
    private String description;

    /*qq*/
    @NotEmpty(message = "qq不能为空")
    private String qq;

    /*qq status 默认为隐藏*/
    private Integer qqStatus = MsgStatusEnum.HIDDEN.getCode();


    /*手机号码*/
    @NotEmpty(message = "手机号码不能为空")
    private String userPhone;

    /*phone status 默认为隐藏*/
    private Integer phoneStatus = MsgStatusEnum.HIDDEN.getCode();

    /*邮箱*/
    @NotEmpty(message = "邮箱不能为空")
    private String userMailbox;

    /*mailbox status 默认为隐藏*/
    private Integer mailboxStatus = MsgStatusEnum.HIDDEN.getCode();


}
