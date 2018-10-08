package com.cinsc.MainView.ctr;

import com.cinsc.MainView.annotation.CheckPermission;
import com.cinsc.MainView.annotation.enums.PermsEnum;
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

    //TODO 增加修改用户和角色对应关系的接口(关系: 一一对应)

    /**
     * 新增用户
     * @param sysUserFrom
     * @param result
     * @return
     */
    @CheckPermission(perms = PermsEnum.TEACHER)
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
     * 查询用户详情
     * @param id
     * @return
     */
    @CheckPermission(perms = PermsEnum.TEACHER)
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
    @CheckPermission(perms = PermsEnum.TEACHER)
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
    @CheckPermission(perms = PermsEnum.TEACHER)
    @GetMapping("/deleteUser/{id}")
    public ResultVo deleteUser(@PathVariable Integer id){
        return sysUserService.deleteUser(id);
    }



}

