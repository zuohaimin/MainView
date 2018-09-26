package com.cinsc.MainView.utils.convert;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @Author: 束手就擒
 * @Date: 18-8-21 下午2:30
 * @Description:
 */
@Slf4j
public class PictureToBase64 {

    public static String getImageStr(String imageFile){
        InputStream in = null;
        byte[] data = null;
        try{
            in = new FileInputStream(imageFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        log.info("图片转码 data={}",data);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    //base64字符串转化成图片
    public static boolean GenerateImage(String imgStr)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "d://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
