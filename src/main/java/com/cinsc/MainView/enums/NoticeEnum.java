package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:57
 * @Description:
 */
@Getter
public enum NoticeEnum {
    UNREAD(0,"未读"),
    READ(1,"已读")
    ;
    private Integer code;
    private String msg;

    NoticeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
