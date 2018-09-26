package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.SMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 束手就擒
 * @Date: 18-8-4 下午1:59
 * @Description:
 */
@Repository
public interface SMSRepository extends JpaRepository<SMS,String> {
}
