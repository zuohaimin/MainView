package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.dto.UserDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.repository.RoleRepository;
import com.cinsc.MainView.repository.UserLoginRepository;
import com.cinsc.MainView.repository.UserRoleRepository;
import com.cinsc.MainView.service.UserService;

import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;


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

    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserLoginRepository userLoginRepository,
                           UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository){
       this.userLoginRepository = userLoginRepository;
       this.userRoleRepository = userRoleRepository;
       this.roleRepository = roleRepository;
    }


    @Override
    public UserLogin findByAccount(String account) {
        return userLoginRepository.findByUserAccount(account);
    }

    @Override
    public ResultVo saveUser(UserDto sysUserFrom) {

        /*判断该账号是否存在*/
        if (userLoginRepository.findByUserAccount(sysUserFrom.getAccount()) != null){
            log.error(ResultEnum.UNkNOWN_ACCOUNT.getMessage()+"sysUserFrom",sysUserFrom);
            return ResultVoUtil.erro(ResultEnum.ACCOUNT_EXIST.getCode(),ResultEnum.ACCOUNT_EXIST.getMessage());
        }

        /*分离用户基本信息与其角色*/
        UserLogin sysUser = new UserLogin();
        BeanUtils.copyProperties(sysUserFrom,sysUser);


        /*生成盐以及加密码并保存*/
        String salt = ShiroUtil.getSalt();
        String md5Password = ShiroUtil.MD5(sysUser.getUserPwd(),salt);
        sysUser.setUserPwd(md5Password);
        sysUser.setUserSalt(salt);
        UserLogin sysUserSave = userLoginRepository.save(sysUser);
        log.info("用户信息成功保存，sysUserSave={}",sysUserSave);

        /*用户对应角色保存*/
        UserRole sysUserRole = new UserRole();
        sysUserRole.setUserId(sysUserSave.getUserId());
        sysUserRole.setRoleId(sysUserFrom.getId());
        UserRole userRoleSave = userRoleRepository.save(sysUserRole);
        log.info("用户对应角色保存成功，userRoleSave={}",userRoleSave);
        return ResultVoUtil.success();
    }



    /**
     *
     * 查询用户详情
     * @param id
     * @return
     */

    @Override
    public ResultVo selectUserDetail(Integer id) {
//        /*查询用户基本信息*/
//
//        UserVo sysUserVo = new UserVo();
//        UserLogin sysUser = userLoginRepository.findById(id).get();
//        BeanUtils.copyProperties(sysUser,sysUserVo);
//        sysUserVo.setPassword("******");
//        log.info("用户基本信息：sysUser = "+sysUser);
//
//        /*取出角色Id*/
//        List<Integer> roleIds = new ArrayList<>();
//        userRoleRepository.findByUserId(id).stream()
//                                            .forEach(o->{
//                                                roleIds.add(o.getRoleId());
//
//        });
//
//        /*查询该用户用户角色*/
//
//        List<Role> sysRoles = roleRepository.findAllById(roleIds);
//        log.info("用户角色：sysRoles = "+ sysRoles);
//        sysUserVo.setSysRoles(sysRoles);
//
//        return ResultVoUtil.success(sysUserVo);
        return null;

    }

    /**
     * 更新用户
     * @param sysUserFrom
     * @return
     */
    @Override
    public ResultVo updateUser(UserDto sysUserFrom) {

        /*判断用户有没有修改账号*/
        if (!sysUserFrom.getAccount().equals(userLoginRepository.findById(sysUserFrom.getId()).get().getUserAccount())){

            //判断修改的账号是否已经存在
            if (findByAccount(sysUserFrom.getAccount()) != null){
                log.error("账号已经存在"+sysUserFrom.getAccount());
                return ResultVoUtil.erro(ResultEnum.ACCOUNT_EXIST.getCode(),ResultEnum.ACCOUNT_EXIST.getMessage());
            }
        }


        /*分离用户与其拥有角色*/
        UserLogin sysUser = new UserLogin();
        BeanUtils.copyProperties(sysUserFrom,sysUser);

        /*初始化密码与盐并保存*/
        String salt = ShiroUtil.getSalt();
        sysUser.setUserSalt(salt);
        String password = ShiroUtil.MD5(sysUser.getUserPwd(),salt);
        sysUser.setUserPwd(password);
        UserLogin sysUserSave = userLoginRepository.save(sysUser);
        log.info("用户更新: sysUserSave={}",sysUserSave);

        /*初始化用户角色*/
        userRoleRepository.deleteByUserId(sysUserFrom.getId());
        /*添加用户角色*/
        UserRole sysUserRole = new UserRole();
        sysUserRole.setUserId(sysUserFrom.getId());
        sysUserRole.setRoleId(sysUserFrom.getId());
        UserRole userRoleSave = userRoleRepository.save(sysUserRole);
        log.info("用户角色: userRoleSave = {}",userRoleSave);
        return ResultVoUtil.success();
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public ResultVo deleteUser(Integer id) {
        userRoleRepository.deleteByUserId(id);
        userLoginRepository.deleteById(id);
        return ResultVoUtil.success();
    }


    @Override
    public Integer getRoleId(HttpServletRequest request) {
        return userRoleRepository.findByUserId(ShiroUtil.getUserId(request)).getRoleId();
    }
}
