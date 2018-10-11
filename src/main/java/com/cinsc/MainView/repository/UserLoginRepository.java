package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:51
 * @Description:
 */
@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Integer>{
    /**
     * 根据账号查询用户
     * @param account
     * @return
     */
    UserLogin findByUserAccount(String account);

    Page<UserLogin> findAll(Specification<UserLogin> sysRoleSpecification, Pageable pageable);
    List<UserLogin> findByUserIdIn(List<Integer> userIdList);
}
