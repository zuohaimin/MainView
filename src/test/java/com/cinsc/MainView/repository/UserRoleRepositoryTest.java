package com.cinsc.MainView.repository;

import com.cinsc.MainView.vo.UserManagerVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: 束手就擒
 * @Date: 18-10-15 下午3:24
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRoleRepositoryTest {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    public void getUserManagerVo() {
        UserManagerVo userManagerVo = userRoleRepository.getUserManagerVo(1);
        Assert.assertNotNull(userManagerVo);
        System.out.println(userManagerVo.toString());
    }
}