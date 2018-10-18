package com.cinsc.MainView.vo;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-10-17 下午7:52
 * @Description:
 */
@Data
public class ArrangeScheduleVo {

    private String arrangeId;

    private String description;

    private Integer status;

    private long deadLine;

    private long createTime;
}
