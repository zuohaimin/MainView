package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: 束手就擒
 * @Date: 18-10-23 下午5:19
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRefineVo {
    private Integer userId;

    private String userName;
}
