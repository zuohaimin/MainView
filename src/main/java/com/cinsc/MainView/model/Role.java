package com.cinsc.MainView.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Entity
@Data
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色等级
     */
    private Integer grade;

    /**
     * 备注
     */
    private String remark;
}
