package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:56
 * @Description:
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>{
    Page<Role> findAll(Specification<Role> specification, Pageable pageable);
}
