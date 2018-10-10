package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-10-10 下午1:46
 * @Description:
 */
@Getter
public enum ArrangeStatusEnum {
    WAIT(0,"未完成"),
    FINISH(1,"已完成")
    ;
    private Integer code;
    private String msg;

    ArrangeStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
