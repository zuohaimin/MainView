package com.cinsc.MainView.exception;


import com.cinsc.MainView.enums.ResultEnum;
import lombok.Getter;

/**
 * 自定义异常类
 * author: 小宇宙
 * date: 2018/4/5
 */
@Getter
public class SystemException extends RuntimeException{

    private Integer code;

    public SystemException(ResultEnum rEnum) {
        super(rEnum.getMessage());

        this.code = rEnum.getCode();
    }

    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public SystemException(String message) {
        super(message);
    }
}
