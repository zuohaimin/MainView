package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Cc;
import com.cinsc.MainView.model.Transactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-16 下午6:59
 * @Description:
 */
@Repository
public interface CcRepository extends JpaRepository<Cc,Integer>{
    List<Cc> findByUserId(Integer userId);
}
