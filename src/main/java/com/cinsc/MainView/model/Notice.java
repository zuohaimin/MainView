package com.cinsc.MainView.model;

import com.cinsc.MainView.enums.NoticeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:51
 * @Description:
 */
@Data
@Entity
public class Notice {

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
    //TODO 解耦
    private String title = "无标题";

}
