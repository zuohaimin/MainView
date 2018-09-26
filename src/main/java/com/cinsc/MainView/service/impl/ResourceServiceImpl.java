package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.model.Resource;
import com.cinsc.MainView.model.RoleResource;
import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.repository.ResourceRepository;
import com.cinsc.MainView.repository.RoleResourceRepository;
import com.cinsc.MainView.repository.UserRoleRepository;
import com.cinsc.MainView.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: 束手就擒
 * @Date: 18-7-15 下午2:49
 * @Description:
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private UserRoleRepository sysUserRoleRepository;
    private RoleResourceRepository sysRoleResourceRepository;
    private ResourceRepository sysResourceRepository;

    @Autowired
    public ResourceServiceImpl(UserRoleRepository sysUserRoleRepository,
                               RoleResourceRepository sysRoleResourceRepository,
                               ResourceRepository sysResourceRepository){
        this.sysUserRoleRepository = sysUserRoleRepository;
        this.sysRoleResourceRepository = sysRoleResourceRepository;
        this.sysResourceRepository = sysResourceRepository;
    }


    /**
     * 查询用户权限表示符
     * @param userId
     * @return
     */
    @Override
    public Set<String> seleteUserPerms(Integer userId) {
        /*获取 角色Id*/
        List<Integer> roleIds = new ArrayList<>();
        List<UserRole> sysRoleList = sysUserRoleRepository.findByUserId(userId);
        sysRoleList.stream().forEach(o->{
            roleIds.add(o.getRoleId());
        });
        /*获取资源Id*/
        List<RoleResource> sysRoleResourceList = sysRoleResourceRepository.findByRoleId(roleIds);
        List<Integer> resourceIds = new ArrayList<>();
        sysRoleResourceList.stream().forEach(o->{
            resourceIds.add(o.getResourceId());
        });
        /*获取权限标识符*/
        Set<String> perms = new HashSet<>();
        List<Resource> sysResources = sysResourceRepository.findAllById(resourceIds);
        sysResources.stream().forEach(o->{
            if (StringUtils.isNoneBlank(o.getPerms())){
                perms.add(o.getPerms());
            }

        });
        return perms;
    }
}
