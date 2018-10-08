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
    UserLogin findByAccount(String account);

    ResultVo saveUser(UserDto sysUserFrom);

    ResultVo selectUserDetail(Integer id);

    ResultVo updateUser(UserDto sysUserFrom);

    ResultVo deleteUser(Integer id);


    Integer getRoleId(HttpServletRequest request);

}
