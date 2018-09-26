package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-8-3 下午9:24
 * @Description:
 */
@Getter
public enum MsgStatusEnum {
    HIDDEN(0,"隐藏"),
    OPEN(1,"公开"),
    ;
    private Integer code;
    private String msg;

    MsgStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
