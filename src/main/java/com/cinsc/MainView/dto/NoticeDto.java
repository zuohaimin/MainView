package com.cinsc.MainView.dto;

import com.cinsc.MainView.enums.NoticeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午4:27
 * @Description:
 */
@Data
public class NoticeDto {

    /*接收人账号*/
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "接收人账号不能为空")
    private String to;

    /*消息内容*/
    @Length(max = 128,message = "长度不能超过128位")
    @NotEmpty(message = "消息内容不能为空")
    private String content;
}
