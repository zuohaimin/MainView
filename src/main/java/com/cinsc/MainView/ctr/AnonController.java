package com.cinsc.MainView.ctr;


import com.cinsc.MainView.dto.UserDetailDto;
import com.cinsc.MainView.dto.UserRegistDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.PersonalService;
import com.cinsc.MainView.service.UserRegist;
import com.cinsc.MainView.service.UserService;
import com.cinsc.MainView.utils.Assert;
import com.cinsc.MainView.utils.ResultVoUtil;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Author: 束手就擒
 * @Date: 18-6-4 下午8:08
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/anno")
@Slf4j
@Api(tags = "注册登录页面")
public class AnonController {

    private UserRegist userRegist;


    @Autowired
    public AnonController(UserRegist userRegist){

        this.userRegist = userRegist;

    }


    /**
     * 登录
     * @param userAccount
     * @param password
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVo login(@RequestParam(value = "userAccount",required = false) String userAccount,
                          @RequestParam(value = "password",required = false) String password){
        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        Assert.isBlank(password,"密码不能为空|不能含有空格");
        log.info("账户名 = "+userAccount+" ,密码 = "+password);

        return userRegist.login(password,userAccount);
    }

    /**
     * 判断该注册用户是否存在
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "判断该注册用户是否存在")
    @RequestMapping(value = "/ifNotExitsUserAccount",method = RequestMethod.GET)
    public ResultVo ifNotExitsUserAccount(@RequestParam(value = "userAccount",required = false) String userAccount){

        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        return  userRegist.ifNotExitsUserAccount(userAccount);
    }

    @ApiOperation(value = "注销")
    @GetMapping("logout")
    public ResultVo logout(){
        //TODO 可能有问题
        ShiroUtil.logout();
        return ResultVoUtil.success();
    }

    @ApiOperation(value = "未登录")
    @RequestMapping(value = "/notLogin",method = RequestMethod.GET)
    public ResultVo notLogin(){
        return ResultVoUtil.erro(ResultEnum.NOT_LOGIN.getCode(),ResultEnum.NOT_LOGIN.getMessage());
    }


    /*注册用户*/
    /**
     * 判断该注册用户是否存在
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "判断该注册用户是否存在")
    @RequestMapping(value = "/ifExitsUserAccount",method = RequestMethod.GET)
    public ResultVo ifExitsUserAccount(@RequestParam(value = "userAccount",required = false) String userAccount){

        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        return  userRegist.ifExitsUserAccount(userAccount);
    }

    /**
     * 发送验证码
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "发送验证码")
    @RequestMapping(value = "/sendSMS",method = RequestMethod.GET)
    public ResultVo sendSMS(@RequestParam(value = "userAccount",required = false) String userAccount){
        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        return userRegist.sendSMS(userAccount);
    }

    /**
     * 验证验证码
     * @param userAccount
     * @return
     */
    @ApiOperation(value = "验证验证码")
    @RequestMapping(value = "/checkSMS",method = RequestMethod.GET)
    public ResultVo checkSMS(@RequestParam(value = "userAccount",required = false) String userAccount,
                             @RequestParam(value = "sms",required = false) String sms){
        Assert.isBlank(userAccount,"账户名不能为空|不能含有空格");
        Assert.isBlank(sms,"验证码不能为空|不能含有空格");
        return userRegist.checkSMS(sms,userAccount);
    }

    /**
     * 更新用户密码
     * @param userRegistDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新用户密码")
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@Valid UserRegistDto userRegistDto,
                                   BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("[更新用户密码]参数不正确:userRegistDto={}",userRegistDto);
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }
        return userRegist.updateUserAccount(userRegistDto);
    }

    /**
     * 注册并保存用户账户和密码(userLogin)
     * @param userRegistDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "注册并保存用户账户和密码(userLogin)")
    @RequestMapping(value = "/saveUserAccount",method = RequestMethod.POST)
    public ResultVo saveUserAccount(@Valid @RequestBody UserRegistDto userRegistDto,
                                    BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("[注册用户]参数不正确:userRegistDto={}",userRegistDto);
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }

        return userRegist.regist(userRegistDto);
    }




}
