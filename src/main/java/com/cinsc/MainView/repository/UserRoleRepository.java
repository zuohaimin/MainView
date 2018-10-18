package com.cinsc.MainView.repository;

import com.cinsc.MainView.model.UserRole;
import com.cinsc.MainView.vo.UserManagerVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:55
 * @Description:
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer>{
    void deleteByUserId(Integer userId);
    UserRole findByUserId(Integer userId);

//    @Query(nativeQuery = true,value = "select * from user_detail  d  where d.user_id=?1 union select * from user_login as l where l.user_id=?1")
//    @Query(value = "select new com.cinsc.MainView.vo.UserManagerVo(u.userName,l.userForbidden) from UserDetail u,UserLogin l where u.userId=l.userId and l.userId=:userId")
//@Query(value = "select new com.cinsc.MainView.vo.UserManagerVo(:userId,u.userName,l.userForbidden,r.name) from Role r,UserDetail u,UserLogin l where r.id=:roleId and l.userId=u.userId and l.userId=:userId")
    @Query(value = "select new com.cinsc.MainView.vo.UserManagerVo(u.userId,u.userName,l.userForbidden,r.name) \n" +
            "from Role r,UserRole ur,UserDetail u, UserLogin l \n" +
            "where  \n" +
            "\tr.id=ur.roleId and\n" +
            "\tl.userId=ur.userId and\n" +
            "\tl.userId=u.userId and\n" +
            "\tl.userId=:userId")
    UserManagerVo getUserManagerVo(@Param("userId") Integer userId);

//    @Query(value = "select new com.cinsc.MainView.vo.UserManagerVo(u.userId,u.userName,l.userForbidden,r.name) \n" +
//            "from Role r,UserRole ur,UserDetail u, UserLogin l \n" +
//            "where  \n" +
//            "\tr.id=ur.roleId and\n" +
//            "\tl.userId=ur.userId and\n" +
//            "\tl.userId=u.userId and\n" +
//            "\tl.userId=In(:userIdList)")
//    List<UserManagerVo> getUserManagerVoList(@Param("userIdList") List<Integer> userIdList);
}
