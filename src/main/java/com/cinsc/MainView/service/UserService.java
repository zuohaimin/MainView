package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.UserDto;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.vo.ResultVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-6-11 下午8:06
 * @Description:
 */
public interface UserService {

    /*查找单个用户*/
    ResultVo findByUserName(String userName);

    /*查找所有*/
    ResultVo findAll();

    ResultVo saveUser(UserDto userDto);

    /*配置用户角色关系*/
    ResultVo configureUserRole(Integer roleId, Integer userId);

    /*禁用某个用户*/
    ResultVo forbidUsers(Integer userId);

    /*删除某个用户*/
    ResultVo deleteUser(Integer userId);

    Integer getRoleId(HttpServletRequest request);

    /*获得某用户的权限*/
    ResultVo getUserRole(HttpServletRequest request);

    /*获得权限表*/
    ResultVo getRoleList();

    /*获得全部成员的最简信息*/
    ResultVo getAllUserMsg(HttpServletRequest request);

}
