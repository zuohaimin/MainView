package com.cinsc.MainView.annotation.convert;


import com.cinsc.MainView.annotation.enums.PermsEnum;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.enums.RoleEnum;
import com.cinsc.MainView.exception.SystemException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 束手就擒
 * @Date: 18-10-8 下午4:16
 * @Description:
 */
public class RoleIdToName {

    public static List<String> getRoleString(List<Integer> roleIds){
        List<String> roleString = new ArrayList<>();
        roleIds.forEach(o->{
            if (o.equals(RoleEnum.MANAGER.getCode())){
                roleString.add(PermsEnum.VISITOR.toString());
            }else if (o.equals(RoleEnum.TEACHER.getCode())){
                roleString.add(PermsEnum.TEACHER.toString());
            }else if (o.equals(RoleEnum.GRADUATE.getCode())){
                roleString.add(PermsEnum.GRADUATE.toString());
            }else if (o.equals(RoleEnum.UNDERGRADUATE.getCode())){
                roleString.add(PermsEnum.UNDERGRADUATE.toString());
            }else if (o.equals(RoleEnum.VISITOR.getCode())){
                roleString.add(PermsEnum.VISITOR.toString());
            }else{
                throw new SystemException(ResultEnum.DATA_ERROR);
            }
        });

        return roleString;
    }

    public static Integer getRoleId(String roleName){
        Integer roleId = 0;
        if (PermsEnum.VISITOR.toString().equals(roleName)){
            roleId = 5;
        }else if (PermsEnum.TEACHER.toString().equals(roleName)){
            roleId = 4;
        }else if (PermsEnum.GRADUATE.toString().equals(roleName)){
            roleId = 3;
        }else if (PermsEnum.UNDERGRADUATE.toString().equals(roleName)){
            roleId = 2;
        }else if (PermsEnum.MANAGER.toString().equals(roleName)){
            roleId = 1;
        }else{
            throw new SystemException(ResultEnum.DATA_ERROR);
        }
        return roleId;
    }
}
