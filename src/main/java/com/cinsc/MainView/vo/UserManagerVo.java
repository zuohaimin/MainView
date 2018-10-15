package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @Author: 束手就擒
 * @Date: 18-10-15 下午4:23
 * @Description:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserManagerVo {
    private Integer userId;

    private String userName;

    private Integer userForbidden;

    @JsonProperty(value = "roleName")
    private String name;

    public UserManagerVo(Integer userId, String userName, Integer userForbidden, String name) {
        this.userId = userId;
        this.userName = userName;
        this.userForbidden = userForbidden;
        this.name = name;
    }



}
