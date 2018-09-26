package com.cinsc.MainView.ctr;

import com.cinsc.MainView.dto.RoleDto;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import com.cinsc.MainView.service.RoleService;

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
 * @Date: 18-7-15 下午5:29
 * @Description:
 */
@CrossOrigin
@RequestMapping("sys")
@RestController
@Slf4j
@Api(tags = "管理员后台(角色设置)")
public class SysRoleController {

    private RoleService sysRoleService;

    @Autowired
    public SysRoleController(RoleService sysRoleService){
        this.sysRoleService = sysRoleService;
    }

    /**
     * 新增角色
     * @param sysRoleFrom
     * @param bindingResult
     * @return
     */
    @RequiresPermissions("sys:role:insert")
    @PostMapping("/saveRole")
    public ResultVo saveRole(@Valid @RequestBody RoleDto sysRoleFrom,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("【新增角色】参数不正确:sysRoleFrom={}"+ sysRoleFrom);
            throw new SystemException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        return sysRoleService.saveRole(sysRoleFrom);
    }

    /**
     * 查询角色列表
     * @param page
     * @param size
     * @param name
     * @return
     */
    @RequiresPermissions("sys:role:list")
    @GetMapping("/selectRoleList")
    public ResultVo selectRoleList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "name",defaultValue = "") String name){

        PageRequest pageRequest = new PageRequest(page,size);
        return sysRoleService.selectRoleList(name,pageRequest);
    }

    /**
     * 查询角色详情
     * @param id
     * @return
     */
    @RequiresPermissions("sys:role:detail")
    @GetMapping("/selectRoleDetail")
    public ResultVo selectRoleDetail(@RequestParam(value = "id",required = false)Integer id){
        Assert.isNull(id,"id不能为空");

        return sysRoleService.selectRoleDetail(id);
    }

    /**
     * 新增角色
     * @param sysRoleFrom
     * @param bindingResult
     * @return
     */
    @RequiresPermissions("sys:role:update")
    @PostMapping("/updateRole")
    public ResultVo updateRole(@Valid @RequestBody RoleDto sysRoleFrom,
                             BindingResult bindingResult){

        Assert.isNull(sysRoleFrom.getId(),"id不能为空");

        if(bindingResult.hasErrors()){
            log.error("【更新角色】参数不正确:sysRoleFrom={}"+ sysRoleFrom);
            throw new SystemException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        return sysRoleService.updateRole(sysRoleFrom);
    }
    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequiresPermissions("sys:role:delete")
    @DeleteMapping("/deleteRole/{id}")
    public ResultVo deleteRole(@PathVariable Integer id){


        return sysRoleService.deleteRole(id);
    }

}
