package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.dto.UserRegistDto;
import com.cinsc.MainView.entity.MailEntity;
import com.cinsc.MainView.enums.ForbiddenEnum;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.jwt.JWTUtil;
import com.cinsc.MainView.model.SMS;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.repository.SMSRepository;
import com.cinsc.MainView.repository.UserLoginRepository;
import com.cinsc.MainView.repository.UserRoleRepository;
import com.cinsc.MainView.service.UserRegist;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.mail.JavaSMS;
import com.cinsc.MainView.utils.mail.MailContent;
import com.cinsc.MainView.utils.mail.MailQQlUtil;
import com.cinsc.MainView.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

/**
 * @Author: 束手就擒
 * @Date: 18-8-4 下午1:10
 * @Description:
 */
@Service
@Slf4j
//2018.10.11 检查账户是否禁用
public class UserRegistImpl implements UserRegist{

    private UserLoginRepository userLoginRepository;
    private SMSRepository smsRepository;
    private MailEntity mailEntity;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public UserRegistImpl(UserLoginRepository userLoginRepository,
                          SMSRepository smsRepository,
                          MailEntity mailEntity,
                          UserRoleRepository userRoleRepository){
        this.userLoginRepository = userLoginRepository;
        this.smsRepository = smsRepository;
        this.mailEntity = mailEntity;
        this.userRoleRepository = userRoleRepository;
    }

    @Value("${secret}")
    private String secret;


    /**
     * 赋予用户角色
     * @param userAccount
     */
    private ResultVo giveUserRole(String userAccount){
        UserLogin userLogin = userLoginRepository.findByUserAccount(userAccount);
        if (null == userLogin){
            log.info("[注册] 查询用户 userLogin == null");
            throw new SystemException(ResultEnum.UNkNOWN_ACCOUNT);
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(userLogin.getUserId());
        userRole.setRoleId(MainViewConstant.DEFAULT_ROLE_ID);
        UserRole userRoleSave = userRoleRepository.save(userRole);
        log.info("[注册] 保存用户角色 userRoleSave = {}",userRoleSave);
        return ResultVoUtil.success();
    }

    private boolean isExist(String userAccount){
        return userLoginRepository.findByUserAccount(userAccount) != null;
    }
    /**
     * 验证用户是否存在
     * @param userAccount
     * @return
     */
    @Override
    public ResultVo ifNotExitsUserAccount(String userAccount) {
        if (isExist(userAccount)){
            return ResultVoUtil.success();
        }
        return ResultVoUtil.erro(ResultEnum.UNkNOWN_ACCOUNT);
    }

    /**
     * 登录
     * @param password
     * @param userAccount
     * @return
     */
    @Override
    public ResultVo login(String password, String userAccount) {
        UserLogin userLogin = userLoginRepository.findByUserAccount(userAccount);
        /*验证账户是否禁用*/
        if (userLogin.getUserForbidden().equals(ForbiddenEnum.DISABLE.getCode())){
            log.error("[登录]账户禁用 userForbidden={}",userLogin.getUserForbidden());
            throw new SystemException(ResultEnum.ACCOUNT_DISABLE);
        }
        /*验证密码是否正确*/
        if (!ShiroUtil.MD5(password,userLogin.getUserSalt()).equals(userLogin.getUserPwd())){
            log.error("[登录]密码错误 userLogin={}",userLogin);
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        /*生成Token返回客户端*/
        String token = JWTUtil.sign(userAccount,userLogin.getUserId(),secret);

        return ResultVoUtil.success(token);
    }

    @Override
    public ResultVo ifExitsUserAccount(String userAccount) {
       if (!isExist(userAccount)){
           return ResultVoUtil.success();
       }

        return ResultVoUtil.erro(ResultEnum.ACCOUNT_EXIST);
    }

    @Override
    public ResultVo sendSMS(String userAccount) {
        /*发送验证码*/
        String sms = JavaSMS.getSMS();
        try {

                MailQQlUtil.sendMail(userAccount, MailContent.getContent(sms),mailEntity);

        } catch (MessagingException e) {
           return ResultVoUtil.erro(0,e.getMessage());
        }
        /*存储验证码*/
        /*2018.8.4 存储在MySQL数据库里面 */
        //TODO 存储在redis里面
        SMS sMS = new SMS(userAccount,sms);
        SMS sMSSave = smsRepository.save(sMS);
        log.info("存储验证码 sMSSave={}",sMSSave);
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo checkSMS(String sms, String userAccount){
        smsRepository.findById(userAccount)
                .map(SMS::getSms)
                .filter(o->o.equals(sms))
                .orElseThrow(()->new SystemException(ResultEnum.SMS_ERROR));
        return ResultVoUtil.success();
    }

    /**
     * 装配UserLogin
     * @param password
     * @param userAccount
     * @return
     */
    private UserLogin getUserLogin(String password, String userAccount){
        UserLogin userLogin = new UserLogin();
        userLogin.setUserAccount(userAccount);
        String salt = ShiroUtil.getSalt();
        String pwd = ShiroUtil.MD5(password, salt);
        userLogin.setUserPwd(pwd);
        userLogin.setUserSalt(salt);
        return userLogin;
    }

//    /**
//     * 保存账户密码信息
//     * @param password
//     * @param userAccount
//     * @return
//     */
//    @Override
//    public ResultVo saveUserAccount(String password, String userAccount) {
//        UserLogin userLogin = getUserLogin(password,userAccount);
//        UserLogin userLoginSave = userLoginRepository.save(userLogin);
//        log.info("保存用户账户信息 userLoginSave={}",userLoginSave);
//
//
//        return ResultVoUtil.success();
//    }

    private void saveUserAccount(String password, String userAccount){
        if (isExist(userAccount)){
            log.info("[保存用户账户信息] 用户已经存在");
            throw new SystemException(ResultEnum.ACCOUNT_EXIST);
        }
        UserLogin userLogin = getUserLogin(password,userAccount);
        UserLogin userLoginSave = userLoginRepository.save(userLogin);
        log.info("[保存用户账户信息] userLoginSave={}",userLoginSave);
    }
    /**
     * 更新密码
     * @param userRegistDto
     * @return
     */
    @Override
    public ResultVo updateUserAccount(UserRegistDto userRegistDto) {
        checkSMS(userRegistDto.getSms(),userRegistDto.getUserAccount());
        Integer userId = userLoginRepository.findByUserAccount(userRegistDto.getUserAccount()).getUserId();
        UserLogin userLogin = getUserLogin(userRegistDto.getUserPwd(),userRegistDto.getUserAccount());
        userLogin.setUserId(userId);
        UserLogin userLoginSave = userLoginRepository.save(userLogin);
        log.info("更新用户账户信息 userLoginSave={}",userLoginSave);

        return ResultVoUtil.success();
    }

    @Override
    @Transactional
    public ResultVo regist(UserRegistDto userRegistDto) {
        checkSMS(userRegistDto.getSms(),userRegistDto.getUserAccount());
        saveUserAccount(userRegistDto.getUserPwd(), userRegistDto.getUserAccount());

        return  giveUserRole(userRegistDto.getUserAccount());

    }
}
