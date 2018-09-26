package com.cinsc.MainView.utils;

import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.vo.ResultVo;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午10:08
 * @Description:
 */
public class ResultVoUtil {

    public static <T> ResultVo success(Integer code, String msg, T object){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        resultVo.setData(object);
        return resultVo;
    }

    public static <T> ResultVo success(T object){
        return success(200,"成功",object);
    }
    public static ResultVo success(){
        return success(null);
    }

    public static ResultVo erro(Integer code, String msg){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }
    public static ResultVo erro(ResultEnum rEnum){
        return erro(rEnum.getCode(),rEnum.getMessage());
    }
    public static ResultVo erro(){
        return erro(0,"失败");
    }
}
