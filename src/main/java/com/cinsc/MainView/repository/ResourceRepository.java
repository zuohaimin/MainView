package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午10:00
 * @Description:
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource,Integer>{
}
