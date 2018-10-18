package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-9-27 下午7:05
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeVo implements Serializable{
    private String id;

    private String username;

    private Integer status;

    private String content;

    private String title;

    private long createTime;


}
