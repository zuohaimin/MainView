package com.cinsc.MainView.annotation.enums;

import lombok.Getter;

/**
 * @Author: 束手就擒
 * @Date: 18-10-8 下午3:16
 * @Description:
 */
@Getter
public enum PermsEnum {
    /**
     * 游客
     */
    VISITOR,
    /**
     * 本科生
     */
    UNDERGRADUATE,
    /**
     * 研究生
     */
    GRADUATE,
    /**
     * 老师
     */
    TEACHER,
    /**
     * 管理员
     */
    MANAGER
}
