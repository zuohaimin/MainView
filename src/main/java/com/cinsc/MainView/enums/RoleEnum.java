package com.cinsc.MainView.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-8-3 下午9:02
 * @Description:
 */
@Getter
public enum RoleEnum {

    VISITOR(5,"游客"),
    UNDERGRADUATE(4,"本科生"),
    GRADUATE(3,"研究生"),
    TEACHER(2,"老师"),
    MANAGER(1,"管理员"),
    ;
    private Integer code;
    private String msg;

    RoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
