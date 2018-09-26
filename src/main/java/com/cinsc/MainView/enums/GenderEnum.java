package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-6-19 下午10:22
 * @Description:
 */
@Getter
public enum GenderEnum {
    MALE(1,"male"),
    FEMALE(0,"female")
    ;
    private Integer code;
    private String msg;

    GenderEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
