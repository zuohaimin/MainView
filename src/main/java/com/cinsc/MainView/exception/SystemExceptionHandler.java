package com.cinsc.MainView.exception;

import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.vo.ResultVo;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 束手就擒
 * @Date: 18-7-15 下午4:32
 * @Description:
 */
@RestControllerAdvice
@Slf4j
public class SystemExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    public ResultVo handleAuthenticationException(AuthenticationException exception){
        log.error(ResultEnum.AUTH_ERROR.getMessage());
        return ResultVoUtil.erro(ResultEnum.AUTH_ERROR);
    }

    @ExceptionHandler(SystemException.class)
    public ResultVo handleSystemException(SystemException exception){
        log.error(exception.getMessage());
        return ResultVoUtil.erro(exception.getCode(),exception.getMessage());
    }

    /**
     *缺少权限异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler({AuthorizationException.class})
    public ResultVo handleUnauthorizedException(UnauthorizedException exception){
        log.error(ResultEnum.NOT_PERMSSION.getMessage());
        return ResultVoUtil.erro(ResultEnum.NOT_PERMSSION);
    }

}
