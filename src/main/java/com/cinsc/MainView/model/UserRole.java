package com.cinsc.MainView.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:11
 * @Description:
 */
@Data
@Entity
@DynamicUpdate
public class UserRole {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Integer id;


    /**
     * 用户Id
     */
    private Integer userId;


    /**
     * 角色Id
     */
    private Integer roleId;
}
