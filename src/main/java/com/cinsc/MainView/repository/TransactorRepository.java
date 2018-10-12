package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Transactor;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午7:09
 * @Description:
 */
@Repository
public interface TransactorRepository extends JpaRepository<Transactor,Integer>{
    List<Transactor> findByUserId(Integer userId);
    List<Transactor> findByUserIdAndStatus(Integer userId, Integer status);
    Transactor findByArrangeIdAndUserId(String arrangeId,Integer userId);
    List<Transactor> findByArrangeId(String arrangeId);
    void deleteByArrangeId(String arrangId);
}
