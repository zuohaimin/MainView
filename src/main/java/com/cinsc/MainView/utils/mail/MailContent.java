package com.cinsc.MainView.utils.mail;

/**
 * @Author: 束手就擒
 * @Date: 18-4-18 下午7:23
 * @Description:
 */
//TODO 写一个接口规定邮件的内容格式 使用接口规定
public class MailContent {
    public static String getContent(String sms){
        StringBuilder sb = new StringBuilder();
        sb.append("【束手就擒】你好，这里是****。\n");
        sb.append("你的验证码为：");
        sb.append(sms+"\n");
        return sb.toString();
    }
}
