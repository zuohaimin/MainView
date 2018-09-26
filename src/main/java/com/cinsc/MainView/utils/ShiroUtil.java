package com.cinsc.MainView.utils;

import com.cinsc.MainView.jwt.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;

/**
 * shiro工具
 * author: 小宇宙
 * date: 2018/4/6
 */
public class ShiroUtil {

    /**
     * md5加密方式
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 加密迭代次数
     */
    public final static int hashIterations = 10;

    /**
     * 基于MD5的加密算法
     * @param password
     * @param salt
     * @return
     */
    public static String MD5(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toHex();
    }


    /**
     * 获取当前交互对象
     * @return
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

//    /*从shiro获取当前用户*/
    private static String getAuthorization(HttpServletRequest request){
    return request.getHeader("Authorization");
}
    public static Integer getUserId(HttpServletRequest request){
        return JWTUtil.getUserId(getAuthorization(request));
    }
    /*public static String getUserAccount(HttpServletRequest request){
        return JWTUtil.getUserAccount(getAuthorization(request));
    }
*/
    /**
     * 获取当前会话
     * @return
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 设置Session
     * @param key
     * @param value
     */
    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 获取session信息
     * @param key
     * @return
     */
    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    /**
     * 判断当前用户是否登录
     * @return
     */
    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    /**
     * 退出
     */
    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 获取随机盐
     * @return
     */
    public static String getSalt(){
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }
}
