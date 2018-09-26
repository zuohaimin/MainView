package com.cinsc.MainView.service;

import com.cinsc.MainView.dto.RoleDto;
import com.cinsc.MainView.vo.ResultVo;
import org.springframework.data.domain.Pageable;


/**
 * @Author: 束手就擒
 * @Date: 18-7-15 下午2:40
 * @Description:
 */
public interface RoleService {

    ResultVo saveRole(RoleDto sysRoleFrom);

    ResultVo selectRoleList(String name, Pageable pageable);

    ResultVo selectRoleDetail(Integer id);

    ResultVo updateRole(RoleDto sysRoleFrom);

    ResultVo deleteRole(Integer id);
}
