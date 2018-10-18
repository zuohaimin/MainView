package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:55
 * @Description:
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice,String> {
    List<Notice> findByNoticeTo(String to);
    List<Notice> findByNoticeToAndStatusOrderByCreateTimeDesc(String to,Integer status);
}
