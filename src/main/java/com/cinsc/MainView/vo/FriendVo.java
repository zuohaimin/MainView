package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午5:02
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendVo {

    private String userName;

    private Integer userId;

    private String userIcon;
}
