package com.cinsc.MainView.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: 束手就擒
 * @Date: 18-5-16 下午5:13
 * @Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "mail")
public class MailEntity {

        private Boolean auth ;

        private String host;

        private String port;

        private String user;

        private String password;
}
