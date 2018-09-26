package com.cinsc.MainView.ctr;

import com.cinsc.MainView.dto.UserDetailDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.PersonalService;
import com.cinsc.MainView.utils.Assert;
import com.cinsc.MainView.utils.ShiroUtil;
import com.cinsc.MainView.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @Author: 束手就擒
 * @Date: 18-8-5 下午3:23
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/personal")
@Slf4j
@Api(tags = "用户详情页面")
public class PersonalController {

    private PersonalService personalService;

    @Autowired
    public PersonalController(PersonalService personalService){
        this.personalService = personalService;
    }


    /**
     * 上传头像
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "上传头像")
    @RequestMapping(value = "/uploadIcon", method = RequestMethod.POST)
    public ResultVo uploadIcon(@RequestParam(value = "icon") MultipartFile multipartFile,
                               HttpServletRequest request){
        if (multipartFile.isEmpty()) {
            log.error("上传头像文件为空 multipartFile={}",multipartFile);
            throw new SystemException(ResultEnum.FILE_NOT_EXITS);
        }
        return personalService.uploadIcon(multipartFile, ShiroUtil.getUserId(request));
    }

    /**
     * 保存或者更新 用户详情
     * @param userDetailDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "保存或更新用户详情")
    @RequestMapping(value = "/saveUserDetail", method = RequestMethod.POST)
    public ResultVo saveUserDetail(@Valid UserDetailDto userDetailDto,
                                   BindingResult bindingResult,
                                   HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            log.error("[保存用户详情] userDetailDto = {}",userDetailDto);
            log.error(bindingResult.getFieldError().getDefaultMessage());
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }
        return personalService.saveUserDetail(userDetailDto,request);
    }

    /**
     * 更新用户详情
     * @param userDetailDto
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "更新用户详情")
    @RequestMapping(value = "/updateUserDetail", method = RequestMethod.POST)
    public ResultVo updateUserDetail(@Valid @RequestBody UserDetailDto userDetailDto,
                                   BindingResult bindingResult,
                                     HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            log.error("[更新用户详情] userDetailDto = {}",userDetailDto);
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }
        return personalService.updateUserDetail(userDetailDto,request);
    }


    @ApiOperation(value = "获取用户基本信息")
    @RequestMapping(value = "/getUserMsg", method = RequestMethod.GET)
    public ResultVo getUserMsg(HttpServletRequest request){

        return personalService.getUserMsg(ShiroUtil.getUserId(request));
    }

    @ApiOperation(value = "获取用户详细信息")
    @RequestMapping(value = "/getUserDetail", method = RequestMethod.GET)
    public ResultVo getUserDetail(HttpServletRequest request){

        return personalService.getUserDetail(request);
    }
    /*修改密码可以调用找回密码的接口*/
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public ResultVo changePwd(@RequestParam(value = "oldPwd",required = false) String oldPwd,
                              @RequestParam(value = "newPwd",required = false) String newPwd,
                              HttpServletRequest request){

        Assert.isBlank(oldPwd,"旧密码不能为空|不能含有空格");
        Assert.isBlank(newPwd,"新密码不能为空|不能含有空格");
        return personalService.changePwd(oldPwd,newPwd,request);
    }



    
}
