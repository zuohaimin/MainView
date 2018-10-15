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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    //2018.10.11 增加修改用户和角色对应关系的接口(关系: 一一对应)

    /**
     * 新增用户
     * @param userDto
     * @param result
     * @return
     */
    @ApiOperation(value = "新增用户")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    public ResultVo saveRole(@Valid @RequestBody UserDto userDto,
                             BindingResult result){
        if (result.hasErrors()){
            log.error("[新增用户]参数不正确:userDto={}",userDto);
            throw new SystemException(ResultEnum.PARAM_ERROR.getCode(),result.getFieldError().getDefaultMessage());
        }
        return sysUserService.saveUser(userDto);
    }

    @ApiOperation(value = "查找单个用户")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @GetMapping("/findByUserName")
    public ResultVo findByUserName(@RequestParam(value = "userName",required = false)String userName){
        Assert.isBlank(userName,"userName不能为空|含有空格");
        return sysUserService.findByUserName(userName);
    }

    @ApiOperation(value = "查找所有用户基本信息")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @GetMapping("/findAll")
    public ResultVo findAll(){
        return sysUserService.findAll();
    }

    @ApiOperation(value = "禁用用户(数组)")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @GetMapping("/forbidUsers")
    public ResultVo forbidUsers(@RequestParam(value = "userId",required = false)Integer userId){
        Assert.isNull(userId,"userId不能为空");
        return sysUserService.forbidUsers(userId);
    }

    @ApiOperation(value = "配置用户角色关系")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @GetMapping("/configureUserRole")
    public ResultVo configureUserRole(@RequestParam(value = "roleId",required = false)Integer roleId,
                                      @RequestParam(value = "userId",required = false)Integer userId){
        Assert.isNull(roleId,"roleId不能为空");
        Assert.isNull(userId,"userId不能为空");
        return sysUserService.configureUserRole(roleId,userId);
    }
    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiOperation(value = "删除用户")
    @CheckPermission(perms = PermsEnum.MANAGER)
    @DeleteMapping("/deleteUser/{id}")
    public ResultVo deleteUser(@PathVariable Integer id){
        return sysUserService.deleteUser(id);
    }

    @ApiOperation(value = "获得某用户的权限")
    @GetMapping("/getUserRole")
    public ResultVo getUserRole(HttpServletRequest request){
        return sysUserService.getUserRole(request);
    }

    @ApiOperation(value = "获得权限表")
    @GetMapping("/getRoleList")
    @CheckPermission(perms = PermsEnum.MANAGER)
    public ResultVo getRoleList(){
        return sysUserService.getRoleList();
    }



}