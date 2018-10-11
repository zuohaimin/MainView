package com.cinsc.MainView.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-8-16 下午9:02
 * @Description:
 */
@Data
public class ArrangeVo {

    private String arrangeId;

    private String userName;

    private String description;

    private Date deadLine;

    /*总人数*/
    private Integer totalNum;

    /*完成人数*/
    private Integer finishNum;


}
