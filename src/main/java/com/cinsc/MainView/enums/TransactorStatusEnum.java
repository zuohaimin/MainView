package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午7:03
 * @Description:
 */
@Getter
public enum TransactorStatusEnum {

    READY(0,"待完成"),
    FINISHED(1,"已完成")
    ;

    private Integer code;

    private String msg;

    TransactorStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
