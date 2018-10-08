package com.cinsc.MainView.annotation;

import com.cinsc.MainView.annotation.enums.PermsEnum;


import java.lang.annotation.*;

import static com.cinsc.MainView.annotation.enums.PermsEnum.VISITOR;

/**
 * @Author: 束手就擒
 * @Date: 18-10-8 下午3:03
 * @Description:
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    PermsEnum perms() default VISITOR;
}
