package com.cinsc.MainView.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-8-14 下午2:16
 * @Description:
 */
@Data
public class ArrangeDto {

    @Length(max = 128,message = "长度不能超过128位")
    @NotEmpty(message = "任务信息不能为空")
    private String msg;

    private Integer days;

//    /*抄送人Id集合*/
//    private List<Integer> carbonCopyIdList;


    /*执行人Id集合*/
    private List<Integer> transactorIdList;



}
