package com.cinsc.MainView.utils.mail;

import com.cinsc.MainView.entity.MailEntity;
import org.springframework.beans.factory.annotation.Autowired;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Author: 束手就擒
 * @Date: 18-4-12 下午8:39
 * @Description:
 */
public class MailQQlUtil {



    private static Session  getSession(MailEntity mailEntity){


        Properties props = new Properties();
        props.put("mail.smtp.auth",mailEntity.getAuth());
        props.put("mail.smtp.host",mailEntity.getHost());
        props.put("mail.smtp.port",mailEntity.getPort());
        props.put("mail.user",mailEntity.getUser());
        props.put("mail.password",mailEntity.getPassword());
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailEntity.getUser(),mailEntity.getPassword());
            }
        };

        return Session.getInstance(props,authenticator);
    }


    public static void sendMail(String to, String content, MailEntity mailEntity) throws MessagingException {
        System.out.println(mailEntity);
        MimeMessage message = new MimeMessage(getSession(mailEntity));
        InternetAddress from = new InternetAddress(mailEntity.getUser());

        InternetAddress TO = new InternetAddress(to);

        message.setFrom(from);
        message.setRecipient(MimeMessage.RecipientType.TO, TO);
        message.setSubject("测试邮件");
        message.setContent(content,"text/html;charset=UTF-8");
        Transport.send(message);
    }
}