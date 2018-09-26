package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:55
 * @Description:
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer>{
    void deleteByUserId(Integer userId);
    List<UserRole> findByUserId(Integer userId);
}
