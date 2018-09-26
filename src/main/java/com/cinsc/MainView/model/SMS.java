package com.cinsc.MainView.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 束手就擒
 * @Date: 18-8-4 下午1:56
 * @Description:
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "sms")
public class SMS {

    /*账户*/
    @Id
    private String userAccount;
    /*验证码*/
    private String sms;
}
