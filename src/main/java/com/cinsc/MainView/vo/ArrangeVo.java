package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-8-16 下午9:02
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArrangeVo {

    private String arrangeId;

    private String userName;

    private String description;

    private long deadLine;

    //2018.10.17 添加起始时间
    private long createTime;

    /*总人数*/
    private Integer totalNum;

    /*完成人数*/
    private Integer finishNum;


}
