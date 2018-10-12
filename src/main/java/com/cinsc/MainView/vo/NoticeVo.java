package com.cinsc.MainView.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: 束手就擒
 * @Date: 18-9-27 下午7:05
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeVo {
    private String id;

    private String noticeFrom;

    private String content;

    private String title;

    private String username;
}
