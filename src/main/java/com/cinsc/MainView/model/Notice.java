package com.cinsc.MainView.model;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.enums.NoticeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:51
 * @Description:
 */
@Data
@Entity
public class Notice implements Serializable{

    @Id
    private String id;

    /*发送人账号*/
    private String noticeFrom;

    /*接收人账号*/
    private String noticeTo;

    /*消息状态*/
    @JsonIgnore
    private Integer status = NoticeEnum.UNREAD.getCode();

    /*消息内容*/
    private String content;

    /*标题*/
    private String title = MainViewConstant.NOTICE_DEFAULT_TITLE;

    /*创建时间*/
    private Date createTime = new Date();

}
