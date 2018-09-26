package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.RoleResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:58
 * @Description:
 */
@Repository
public interface RoleResourceRepository extends JpaRepository<RoleResource,Integer>{
    List<RoleResource> findByRoleId(Integer roleId);

    List<RoleResource> findByRoleId(List<Integer> roleIds);

    void deleteByRoleId(Integer id);
}
