package com.cinsc.MainView.ctr;

import com.cinsc.MainView.dto.UserDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.UserService;
import com.cinsc.MainView.utils.Assert;
import com.cinsc.MainView.vo.ResultVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: 束手就擒
 * @Date: 18-7-15 下午2:35
 * @Description:
 */
@CrossOrigin
@RequestMapping("sys")
@RestController
@Slf4j
@Api(tags = "管理员后台(用户角色分配)")
public class SysUserController
{
    private UserService sysUserService;
    @Autowired
    public SysUserController(UserService sysUserService){

        this.sysUserService = sysUserService;
    }


    /**
     * 新增用户
     * @param sysUserFrom
     * @param result
     * @return
     */
    @RequiresPermissions("sys:user:insert")
    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    public ResultVo saveRole(@Valid @RequestBody UserDto sysUserFrom,
                             BindingResult result){
        if (result.hasErrors()){
            log.error("[新增用户]参数不正确:sysUserFrom={}",sysUserFrom);
            throw new SystemException(ResultEnum.PARAM_ERROR.getCode(),result.getFieldError().getDefaultMessage());
        }
        return sysUserService.saveUser(sysUserFrom);
    }

    /**
     * 查询用户列表
     * @param page
     * @param size
     * @param name
     * @return
     */
    @RequiresPermissions("sys:user:list")
    @RequestMapping(value = "/selectUserList",method = RequestMethod.GET)
    public ResultVo selectUserList(@RequestParam(value = "page",defaultValue = "0")Integer page,
                                   @RequestParam(value = "size",defaultValue = "10")Integer size,
                                   @RequestParam(value = "name",defaultValue = "")String name){
        PageRequest pageRequest = new PageRequest(page,size);
        return sysUserService.selectUserList(name,pageRequest);
    }

    /**
     * 查询用户详情
     * @param id
     * @return
     */
    @RequiresPermissions("sys:user:detail")
    @GetMapping("/selectUserDetail")
    public ResultVo selectUserDetail(@RequestParam(value = "id",required = false)Integer id){
        Assert.isNull(id,"id不能为空");
        return sysUserService.selectUserDetail(id);
    }

    /**
     * 更新用户
     * @param sysUserFrom
     * @return
     */
    @RequiresPermissions("sys:user:update")
    @PutMapping("/updateUser")
    public ResultVo updateUser(@Valid @RequestBody UserDto sysUserFrom,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("[更新用户]参数错误 sysUserFrom={}",sysUserFrom);
            throw new SystemException(ResultEnum.PARAM_ERROR);
        }
        return sysUserService.updateUser(sysUserFrom);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequiresPermissions("sys:user:delete")
    @GetMapping("/deleteUser/{id}")
    public ResultVo deleteUser(@PathVariable Integer id){
        return sysUserService.deleteUser(id);
    }



}

