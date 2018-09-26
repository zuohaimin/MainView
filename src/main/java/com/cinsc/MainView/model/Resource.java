package com.cinsc.MainView.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-7-24 下午9:12
 * @Description:
 */
@Data
@Entity
@DynamicUpdate
public class Resource {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 资源父id
     * 0代表本身
     */
    private Integer parentId = 0;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 权限标识符
     */
    private String perms;

    /**
     * 类型：0：目录，1：菜单，2：按钮
     */
    private String type;
}
