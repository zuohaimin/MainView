package com.cinsc.MainView.utils.mail;

import java.util.Random;

/**
 * @Author: 束手就擒
 * @Date: 18-4-18 下午7:28
 * @Description:
 */
public class JavaSMS {
    /**
     * 生成验证码（6位）
     * @return
     */
    public synchronized static String getSMS(){
        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000;
        return number.toString();
    }
}
