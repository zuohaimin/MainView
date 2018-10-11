package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Arrange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午7:08
 * @Description:
 */
@Repository
public interface ArrangeRepository extends JpaRepository<Arrange,String> {

    List<Arrange> findByAuthor(Integer author);
    List<Arrange> findByStatus(Integer status);
    Arrange findByArrangeIdAndAuthor(String arrangeId, Integer author);
}
