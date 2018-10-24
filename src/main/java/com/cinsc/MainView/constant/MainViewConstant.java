package com.cinsc.MainView.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: 束手就擒
 * @Date: 18-9-27 下午6:41
 * @Description:
 */
@Component
public class MainViewConstant {
    //2018.10.22 添加final限制
    public static final String NOTICE_DEFAULT_TITLE = "无标题";

    public static final String NOTICE_FRIEND_TITLE = "请求加好友";

    //2018.10.22 注册默认角色为游客
    public static final Integer DEFAULT_ROLE_ID = 5;

    public static final Integer DEFAULT_SCHEDULE_TOTALNUM = 0;

    //2018.10.22 token一小时过期
    public static final Integer TOKEN_EXPIRE_TIME = 60*60*1000;

    //2018.10.22 添加两个配置值
    public static String UPLOADDIR;

    public static String SECRET;

    @Value("${uploadDir}")
    public void setUPLOADDIR(String UPLOADDIR) {
        MainViewConstant.UPLOADDIR = UPLOADDIR;
    }

    @Value("${secret}")
    public void setSECRET(String SECRET) {
        MainViewConstant.SECRET = SECRET;
    }
    public static String getDefaultUploadDir(){
        return UPLOADDIR+"123.jpg";
    }
}
