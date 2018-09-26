package com.cinsc.MainView;

import com.cinsc.MainView.entity.MailEntity;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.utils.convert.PictureToBase64;
import com.cinsc.MainView.utils.key.KeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainViewApplicationTests {

    @Test
    public void context(){

        System.out.println(KeyUtil.getDefaultUploadDir());
    }

}
