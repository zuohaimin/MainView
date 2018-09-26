package com.cinsc.MainView.model;

import com.cinsc.MainView.enums.TransactorStatusEnum;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-8-13 下午6:59
 * @Description:
 */
@Data
@Entity
public class Transactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String arrangeId;

    private Integer userId;

    /*默认为完成*/
    private Integer status = TransactorStatusEnum.READY.getCode();
}
