package com.cinsc.MainView.enums;

/**
 * Http返回信息枚举
 * author: 小宇宙
 * date: 2018/4/5
 */
public enum ResultEnum {

    UNkNOWN_ACCOUNT(1,"用户不存在"),

    PARAM_ERROR(2, "参数不正确"),

    ACCOUNT_EXIST(3,"该账号已存在"),

    USERNAME_OR_PASSWORD_ERROR(4,"用户名或密码错误"),

    ACCOUNT_DISABLE(5,"账号已被禁用"),

    AUTH_ERROR(6,"账户验证失败"),

    NOT_LOGIN(7,"未登录"),

    NOT_PERMSSION(8,"您没有访问该功能的权限"),

    SMS_ERROR(9,"验证码不匹配"),

    //ACCOUNT_NOT_EXIST(10,"该账号不存在"),

    FILE_NOT_EXITS(11,"文件为空"),
    FRIENDLIST_NULL(12,"好友列表为空"),
    DATA_ERROR(13,"数据异常"),
    TOKEN_ERRO(14,"token异常"),
    TOKEN_EXPIRE(15,"token过时"),
    DETAIL_NOT_FOUND(16,"用户详情为空"),
    NOT_FOUND(17,"NOT FOUND");


    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
