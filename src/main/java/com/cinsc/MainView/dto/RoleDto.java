package com.cinsc.MainView.dto;


import com.cinsc.MainView.model.Resource;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * author: 小宇宙
 * date: 2018/4/6
 */
@Data
public class RoleDto {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名
     */
    @Length(max = 32,message = "长度不能超过32位")
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    /**
     * 角色等级
     */
    @Digits(integer=2,fraction=0,message = "等级必须为整数")
    @Range(min = 1,max = 10,message = "等级系数在1到10之间")
    private Integer grade;

    /**
     * 备注
     */
    @Length(max = 64,message = "长度不能超过64位")
    private String remark;

    /**
     * 拥有资源
     */
    private List<Resource> sysResources;
}
