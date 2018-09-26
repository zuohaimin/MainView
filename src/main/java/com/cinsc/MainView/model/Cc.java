package com.cinsc.MainView.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: 束手就擒
 * @Date: 18-8-16 下午6:45
 * @Description:
 */
@Data
@Entity
public class Cc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String arrangeId;

    private Integer userId;
}
