package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:54
 * @Description:
 */
@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail,String>{
    UserDetail findByUserId(Integer userId);
    List<UserDetail> findByUserIdIn(List<Integer> userIdList);
    UserDetail findByUserName(String userName);
    void deleteByUserId(Integer userId);
}
