package com.cinsc.MainView.utils;


import com.cinsc.MainView.exception.SystemException;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验工具类
 * author: 小宇宙
 * date: 2018/4/7
 */
//2018.10.22 ctr层异常添加code
public class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new SystemException(2,message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new SystemException(2,message);
        }
    }
}
