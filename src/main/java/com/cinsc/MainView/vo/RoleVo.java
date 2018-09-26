package com.cinsc.MainView.vo;

import com.cinsc.MainView.model.Resource;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * author: 小宇宙
 * date: 2018/4/7
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVo {

    /**
     * 主键id
     */
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

    /**
     * 拥有资源
     */
    private List<Resource> sysResources;
}
