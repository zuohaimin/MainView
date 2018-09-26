package com.cinsc.MainView.model;

import com.cinsc.MainView.enums.ForbiddenEnum;
import com.cinsc.MainView.utils.ShiroUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:11
 * @Description:
 */
@Data
@Entity
@DynamicUpdate
public class UserLogin {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer userId;

    /* 姓名 2018.8.4*/
    //  private String userName;

    /*账号 / 邮箱*/
    private String userAccount;

    /*密码*/
    @JsonIgnore
    private String userPwd;

    /*盐*/
    @JsonIgnore
    private String userSalt;

    /*是否禁用 0：否；1：是*/
    private Integer userForbidden = ForbiddenEnum.ENABLE.getCode();
}
