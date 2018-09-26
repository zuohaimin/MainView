package com.cinsc.MainView.model;

import com.cinsc.MainView.enums.GenderEnum;
import com.cinsc.MainView.enums.MsgStatusEnum;
import com.cinsc.MainView.enums.RoleEnum;
import com.cinsc.MainView.utils.key.KeyUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:11
 * @Description:
 */
@Data
@Entity
@DynamicUpdate/*动态更新*/
public class UserDetail {

    @Id
    private String detailId;

    /*联系UserLogin*/
    private Integer userId;

    /*姓名*/
    private String userName;

    /*性别*/
    private Integer userGender;

    /*专业*/
    private String userMajor;

    /*学号*/
    private String studentNumber;

    /*籍贯*/
    private String origin;

    /*教育经历*/
    private String education = RoleEnum.UNDERGRADUATE.getMsg();

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
    private String userIcon = KeyUtil.getDefaultUploadDir();
}
