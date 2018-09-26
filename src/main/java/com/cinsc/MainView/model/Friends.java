package com.cinsc.MainView.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-8-10 下午3:46
 * @Description:
 */
@Data
@Entity
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer aId;

    private Integer bId;
}
