package com.cinsc.MainView.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: 束手就擒
 * @Date: 18-8-4 下午1:04
 * @Description:
 */
@Data
public class UserRegistDto {

    /*账户*/
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "账号名称不能为空")
    private String userAccount;

    /*验证码*/
    @Length(max = 6,message = "长度不能超过6位")
    @NotEmpty(message = "验证码名称不能为空")
    private String sms;

    /*密码*/
    @Length(max = 64,message = "长度不能超过64位")
    @NotEmpty(message = "密码名称不能为空")
    private String userPwd;
}
