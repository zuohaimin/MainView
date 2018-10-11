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
    ResultVo findByAccount(String account);

    /*查找所有*/
    ResultVo findAll();

    ResultVo saveUser(UserDto userDto);

    /*配置用户角色关系*/
    ResultVo configureUserRole(Integer roleId, HttpServletRequest request);

//    /*查看详细信息*/
//    ResultVo selectUserDetail(Integer userId);

//    ResultVo updateUser(UserDto sysUserFrom);

    /*禁用某个用户*/
    ResultVo forbidUsers(List<Integer> userIdList);

    /*删除某个用户*/
    ResultVo deleteUser(Integer userId);


    Integer getRoleId(HttpServletRequest request);

}
