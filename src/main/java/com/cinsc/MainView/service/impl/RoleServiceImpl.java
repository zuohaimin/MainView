package com.cinsc.MainView.service.impl;

import com.cinsc.MainView.dto.RoleDto;
import com.cinsc.MainView.model.Resource;
import com.cinsc.MainView.model.Role;
import com.cinsc.MainView.model.RoleResource;
import com.cinsc.MainView.repository.ResourceRepository;
import com.cinsc.MainView.repository.RoleRepository;
import com.cinsc.MainView.repository.RoleResourceRepository;
import com.cinsc.MainView.service.RoleService;

import com.cinsc.MainView.utils.JPAUtil;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.vo.ResultVo;
import com.cinsc.MainView.vo.RoleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-15 下午2:48
 * @Description:
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository sysRoleRepository;
    private RoleResourceRepository sysRoleResourceRepository;
    private ResourceRepository sysResourceRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository sysRoleRepository,
                           RoleResourceRepository sysRoleResourceRepository,
                           ResourceRepository sysResourceRepository){
        this.sysRoleRepository = sysRoleRepository;
        this.sysRoleResourceRepository = sysRoleResourceRepository;
        this.sysResourceRepository= sysResourceRepository;

    }

    /**
     * 保存角色信息
     * @param sysRoleFrom
     * @return
     */
    @Override
    public ResultVo saveRole(RoleDto sysRoleFrom) {
        /*分离对象并保存*/
        Role sysRole = new Role();
        BeanUtils.copyProperties(sysRoleFrom,sysRole);
        Role sysRoleSave = sysRoleRepository.save(sysRole);
        log.info("角色基本信息保存: sysRoleSave={}",sysRoleSave);

        /*构建该角色的资源并保存*/
        List<RoleResource> sysRoleResources = new ArrayList<>();
        sysRoleFrom.getSysResources().stream().forEach(o->{
            RoleResource sysRoleResource = new RoleResource();
            sysRoleResource.setRoleId(sysRoleFrom.getId());
            sysRoleResource.setResourceId(o.getId());
            sysRoleResources.add(sysRoleResource);
        });
        List<RoleResource> sysRoleResourcesSave = sysRoleResourceRepository.saveAll(sysRoleResources);
        log.info("角色资源保存: sysRoleResourcesSave={}",sysRoleResourcesSave);
        return ResultVoUtil.success();
    }

    /**
     * 查询角色列表
     * @param name
     * @param pageable
     * @return
     */
    @Override
    public ResultVo selectRoleList(String name, Pageable pageable) {
        Specification<Role> specification = (Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNoneBlank(name)){
                predicates.add(criteriaBuilder.like(root.get("name").as(String.class), JPAUtil.like(name)));
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(pre)).getRestriction();
        };
        return ResultVoUtil.success(sysRoleRepository.findAll(specification,pageable));
    }

    /**
     * 查询角色详情
     * @param id
     * @return
     */
    @Override
    public ResultVo selectRoleDetail(Integer id) {
        /*查询角色基本信息*/
        Role sysRole = sysRoleRepository.findById(id).get();
        RoleVo sysRoleVo = new RoleVo();
        BeanUtils.copyProperties(sysRole,sysRoleVo);
        /*取出角色id*/
        List<RoleResource> sysRoleResources = sysRoleResourceRepository.findByRoleId(id);
        List<Integer> resourceIds = new ArrayList<>();
        sysRoleResources.stream().forEach(o->{
            resourceIds.add(o.getResourceId());
        });
        /*取出资源*/
        List<Resource> sysResources = sysResourceRepository.findAllById(resourceIds);
        sysRoleVo.setSysResources(sysResources);
        log.info("角色详情:sysRoleVo = {}",sysRoleVo);
        return ResultVoUtil.success(sysRoleVo);
    }

    /**
     * 更新角色信息
     * @param sysRoleFrom
     * @return
     */

    @Override
    public ResultVo updateRole(RoleDto sysRoleFrom) {

        log.info("初始化角色资源...");
        /*初始化角色资源*/
        sysRoleResourceRepository.deleteByRoleId(sysRoleFrom.getId());

        /*保存*/
        return saveRole(sysRoleFrom);
    }

    /**
     * 删除角色信息
     * @param id
     * @return
     */
    @Override
    public ResultVo deleteRole(Integer id) {
        sysRoleResourceRepository.deleteByRoleId(id);
        sysRoleRepository.deleteById(id);
        return ResultVoUtil.success();
    }
}
