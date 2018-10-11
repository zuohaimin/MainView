package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.dto.UserDto;
import com.cinsc.MainView.enums.ForbiddenEnum;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.model.UserDetail;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.repository.RoleRepository;
import com.cinsc.MainView.repository.UserDetailRepository;
import com.cinsc.MainView.repository.UserLoginRepository;
import com.cinsc.MainView.repository.UserRoleRepository;
import com.cinsc.MainView.service.UserService;

import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.convert.PictureToBase64;
import com.cinsc.MainView.vo.ResultVo;
import com.cinsc.MainView.vo.UserDetailVo;
import com.cinsc.MainView.vo.UserMsgVo;
import com.cinsc.MainView.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: 束手就擒
 * @Date: 18-6-11 下午8:11
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserLoginRepository userLoginRepository;

    private UserRoleRepository userRoleRepository;

    private UserDetailRepository userDetailRepository;


    @Autowired
    public UserServiceImpl(UserLoginRepository userLoginRepository,
                           UserRoleRepository userRoleRepository,
                           UserDetailRepository userDetailRepository){
       this.userLoginRepository = userLoginRepository;
       this.userRoleRepository = userRoleRepository;
       this.userDetailRepository = userDetailRepository;
    }


    /**
     * 查找用户, 获得用户userId
     * @param account
     * @return
     */
    @Override
    public ResultVo findByAccount(String account) {
        UserLogin userLogin = userLoginRepository.findByUserAccount(account);
        if (null == userLogin){
            log.info("管理员页面 [查找单个用户] userLogin == null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        return ResultVoUtil.success(userLogin.getUserId());
    }

    @Override
    public ResultVo findAll() {
        List<UserMsgVo> userMsgVoList = new ArrayList<>();
        userDetailRepository.findAll().forEach(o->{
            UserMsgVo userMsgVo = new UserMsgVo();
            BeanUtils.copyProperties(o,userMsgVo);
            userMsgVo.setUserIcon(PictureToBase64.getImageStr(o.getUserIcon()));
            userMsgVoList.add(userMsgVo);
        });
        return ResultVoUtil.success(userMsgVoList);
    }

    @Override
    public ResultVo saveUser(UserDto userDto) {

        /*判断该账号是否存在*/
        if (null != userLoginRepository.findByUserAccount(userDto.getAccount())){
            log.info("管理员页面[添加用户] 添加账户已经注册");
            throw new SystemException(ResultEnum.DATA_ERROR);
        }

        /*生成盐以及加密码并保存*/
        String salt = ShiroUtil.getSalt();
        UserLogin userLogin = new UserLogin();
        userLogin.setUserSalt(salt);
        userLogin.setUserPwd(ShiroUtil.MD5(userDto.getPassword(),salt));
        userLogin.setUserAccount(userDto.getAccount());
        UserLogin userLoginSave = userLoginRepository.save(userLogin);
        log.info("管理员页面[添加用户] userLoginSave = {}",userLoginSave);
        /*分离用户基本信息与其角色并保存*/
        UserRole userRole = new UserRole();
        userRole.setRoleId(userDto.getRoleId());
        userRole.setUserId(userLoginSave.getUserId());
        UserRole userRoleSave = userRoleRepository.save(userRole);
        log.info("管理员页面[添加用户] userRoleSave = {}",userRoleSave);
       return ResultVoUtil.success();
    }

    @Override
    public ResultVo configureUserRole(Integer roleId, HttpServletRequest request) {
        UserRole userRole = userRoleRepository.findByUserId(ShiroUtil.getUserId(request));
        if (null == userRole){
            log.info("[配置用户角色关系] 该用户不存在角色");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        userRole.setRoleId(roleId);
        UserRole userRoleSave = userRoleRepository.save(userRole);
        log.info("管理员页面 [配置用户角色关系] userRoleSave={}",userRoleSave);
        return ResultVoUtil.success();
    }



    @Override
    public ResultVo forbidUsers(List<Integer> userIdList) {
        List<UserLogin> userLoginList = userLoginRepository.findByUserIdIn(userIdList);
        if (null == userLoginList){
            log.info("管理员页面 [禁用用户] userLoginList == null");
            throw new SystemException(ResultEnum.NOT_FOUND);
        }
        userLoginList.forEach(o->o.setUserForbidden(ForbiddenEnum.DISABLE.getCode()));
        List<UserLogin> userLoginListSave = userLoginRepository.saveAll(userLoginList);
        log.info("管理员页面 [禁用用户] userLoginListSave = {}",userLoginListSave);
        return ResultVoUtil.success();
    }


    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Override
    public ResultVo deleteUser(Integer userId) {
        userRoleRepository.deleteByUserId(userId);
        userLoginRepository.deleteById(userId);
        return ResultVoUtil.success();
    }


    @Override
    public Integer getRoleId(HttpServletRequest request) {
        return userRoleRepository.findByUserId(ShiroUtil.getUserId(request)).getRoleId();
    }
}
