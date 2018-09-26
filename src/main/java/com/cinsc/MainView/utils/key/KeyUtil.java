package com.cinsc.MainView.utils.key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Author: 束手就擒
 * @Date: 18-3-22 下午7:18
 * @Description:
 */
@Component
public class KeyUtil {

    private static String uploadDir;

    @Value("${uploadDir}")
    public void setUploadDir(String dir){
        uploadDir = dir;
    }

    public static String getDefaultUploadDir(){
        return uploadDir+"123.jpg";
    }
    public static String getUploadDir(){
        return uploadDir;
    }




    /**
     * 生成唯一主键
     * 格式：时间 + 随机数
     * @return
     */
    public static synchronized String genUniqueKey(){

        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);

    }

    public static String getMD5(String source) {
        StringBuilder result = new StringBuilder();
        try{

            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(source.getBytes());
            for(byte b : bytes){
                String hex = Integer.toHexString(b&0xff);
                if(hex.length()==1){
                    result.append("0"+hex);

                }else{
                    result.append(hex);
                }
            }
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();

        }


        return result.toString();
    }
}
