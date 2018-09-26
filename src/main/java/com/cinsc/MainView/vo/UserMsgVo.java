package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: 束手就擒
 * @Date: 18-8-17 下午6:14
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMsgVo {

    private Integer userId;

    private String userName;

    private String userMajor;

    private String studentNumber;

    private String userIcon;


}
