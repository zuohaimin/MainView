package com.cinsc.MainView.model;

import com.cinsc.MainView.enums.ArrangeStatusEnum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午6:52
 * @Description:
 */
@Data
@Entity
public class Arrange {

    @Id
    private String arrangeId;

    /*发起人id*/
    private Integer author;

    /*安排描述*/
    private String description;

    /*总人数*/
    private Integer totalNum;

    /*完成人数*/
    private Integer finishNum = 0;

    /*完成状态*/
    private Integer status = ArrangeStatusEnum.WAIT.getCode();

    /*创建时间*/
    private Date createTime;

    /*截止时间*/
    private Date deadLine;
}
