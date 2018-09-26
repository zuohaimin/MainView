package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:49
 * @Description:
 */
@Repository
public interface FriendsRepository extends JpaRepository<Friends,Integer> {
    Friends findByAIdAndBId(Integer AId, Integer BId);
    List<Friends> findByAId(Integer AId);
    void deleteByAIdAndBId(Integer AId, Integer BId);


}
