package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.dto.UserDetailDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.model.UserDetail;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.repository.UserDetailRepository;
import com.cinsc.MainView.repository.UserLoginRepository;
import com.cinsc.MainView.repository.UserRoleRepository;
import com.cinsc.MainView.service.PersonalService;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.convert.PictureToBase64;
import com.cinsc.MainView.utils.key.KeyUtil;
import com.cinsc.MainView.vo.ResultVo;
import com.cinsc.MainView.vo.UserDetailVo;
import com.cinsc.MainView.vo.UserMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: 束手就擒
 * @Date: 18-8-5 下午4:52
 * @Description:
 */
@Service
@Slf4j
public class PersonalServiceImpl implements PersonalService {

    private UserDetailRepository userDetailRepository;
    private UserLoginRepository userLoginRepository;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public PersonalServiceImpl(UserDetailRepository userDetailRepository,
                               UserLoginRepository userLoginRepository,
                               UserRoleRepository userRoleRepository){
        this.userDetailRepository = userDetailRepository;
        this.userLoginRepository = userLoginRepository;
        this.userRoleRepository = userRoleRepository;
    }



    @Override
    @Transactional
    public ResultVo uploadIcon(MultipartFile multipartFile, Integer userId) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = MainViewConstant.UPLOADDIR;
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        String userIcon = filePath+fileName;
        File dest = new File(userIcon);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
                multipartFile.transferTo(dest);
                log.info("上传成功后的文件路径未：" + userIcon);
        } catch (IllegalStateException | IOException e) {
            throw new SystemException(e.getMessage());

        }
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        if (userDetail == null){
            log.info("用户详情为空 userDetail == null");
            throw new SystemException(ResultEnum.DETAIL_NOT_FOUND);
        }
        //2018.9.19
        //删除原有的头像
        log.info("图片路径：",userDetail.getUserIcon());
        if (!MainViewConstant.getDefaultUploadDir().equals(userDetail.getUserIcon())){
            if (new File(userDetail.getUserIcon()).delete()){
              log.info("[上传头像] 删除原有图片成功");
            }
        }
        userDetail.setUserIcon(userIcon);
        UserDetail userDetailSave = userDetailRepository.save(userDetail);
        log.info("上传头像 userDetailSave = {}",userDetailSave);
        return ResultVoUtil.success();

    }

    @Override
    @Transactional
    public ResultVo saveUserDetail(UserDetailDto userDetailDto, HttpServletRequest request) {
        UserDetail userDetail = new UserDetail();
        BeanUtils.copyProperties(userDetailDto,userDetail);
        Integer userId = ShiroUtil.getUserId(request);
        userDetail.setUserId(userId);
        /*2018.8.26 更新保存合并*/
        UserDetail userDetailFind = userDetailRepository.findByUserId(userId);

        if (userDetailFind == null){
            userDetail.setDetailId(UUID.randomUUID().toString());
            log.info("保存用户详情 执行保存操作");
        }else{
            userDetail.setDetailId(userDetailFind.getDetailId());
            //2018.9.19
            userDetail.setUserIcon(userDetailFind.getUserIcon());
            log.info("保存用户详情 执行更新操作");
        }
        UserDetail userDetailSave =  userDetailRepository.save(userDetail);
        log.info("保存用户详情 userDetailSave={}",userDetailSave);
        return ResultVoUtil.success();
    }



    @Override
    public ResultVo getUserMsg(Integer userId) {
        UserMsgVo userMsgVo = new UserMsgVo();
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        log.info("获取角色详情 userDetail={}",userDetail);
        if (userDetail == null){
            throw new SystemException(ResultEnum.DETAIL_NOT_FOUND);
        }
        BeanUtils.copyProperties(userDetail,userMsgVo);
        String data = PictureToBase64.getImageStr(userDetail.getUserIcon());
        userMsgVo.setUserIcon(data);
        return ResultVoUtil.success(userMsgVo);
    }


    @Override
    public ResultVo getUserDetail(HttpServletRequest request) {
        Integer userId = ShiroUtil.getUserId(request);
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        log.info("获取用户详情 userDetail = {}",userDetail);
        if (userDetail == null){
            throw new SystemException(ResultEnum.DETAIL_NOT_FOUND);
        }
        UserDetailVo userDetailVo = new UserDetailVo();
        BeanUtils.copyProperties(userDetail,userDetailVo);
        String userIcon = PictureToBase64.getImageStr(userDetail.getUserIcon());
        userDetailVo.setUserIcon(userIcon);
        return ResultVoUtil.success(userDetailVo);
    }

    @Override
    @Transactional
    public ResultVo changePwd(String oldPwd, String newPwd, HttpServletRequest request) {
        Integer userId = ShiroUtil.getUserId(request);
        UserLogin userLogin = userLoginRepository.findById(userId).orElseThrow(()->new SystemException(ResultEnum.UNkNOWN_ACCOUNT));
        log.info("修改密码 userId = {}",userId);
        String oldPwdMd5 = ShiroUtil.MD5(oldPwd,userLogin.getUserSalt());
        if (!oldPwdMd5.equals(userLogin.getUserPwd())){
            log.error("密码不一致 oldPwd={}",oldPwd);
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        String newSalt = ShiroUtil.getSalt();
        userLogin.setUserSalt(newSalt);
        userLogin.setUserPwd(ShiroUtil.MD5(newPwd,newSalt));
        /*保存密码*/
        UserLogin userLoginSave = userLoginRepository.save(userLogin);
        log.info("用户信息保存 userLoginSave = {}",userLoginSave);
        return ResultVoUtil.success();
    }
}
