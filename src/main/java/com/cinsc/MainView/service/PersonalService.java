package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.UserDetailDto;
import com.cinsc.MainView.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: 束手就擒
 * @Date: 18-8-5 下午4:46
 * @Description:
 */
public interface PersonalService {

    /*上传头像*/
    ResultVo uploadIcon(MultipartFile multipartFile, Integer userId);

    /*保存用户详情*/
    ResultVo saveUserDetail(UserDetailDto userDetailDto,HttpServletRequest request);

    /*更新用户详情*/
    ResultVo updateUserDetail(UserDetailDto userDetailDto,HttpServletRequest request);

    /*获取用户基本信息*/
    ResultVo getUserMsg(Integer userId);

    /*获取用户详细信息*/
    ResultVo getUserDetail(HttpServletRequest request);

    /*修改密码*/
    ResultVo changePwd(String oldPwd, String newPwd, HttpServletRequest request);
}
