package com.lx.project.demo3.controller.sys;

import com.lx.project.demo3.model.Response;
import com.lx.project.demo3.model.sys.UmsPermission;
import com.lx.project.demo3.model.sys.UmsRole;
import com.lx.project.demo3.service.sys.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户角色管理
 * Created by macro on 2018/9/30.
 */
@Controller
@Api(tags = "UmsRoleController", description = "后台用户角色管理")
@RequestMapping("/role")
public class UmsRoleController {
    @Autowired
    private UmsRoleService roleService;

    @ApiOperation("添加角色")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Object create(@RequestBody UmsRole role) {
        int count = roleService.create(role);
        if(count>0){
            return new Response().success(count);
        }
        return new Response().failure();
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object update(@PathVariable Long id, @RequestBody UmsRole role) {
        int count = roleService.update(id,role);
        if(count>0){
            return new Response().success(count);
        }
        return new Response().failure();
    }

    @ApiOperation("批量删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if(count>0){
            return new Response().success(count);
        }
        return new Response().failure();
    }

    @ApiOperation("获取相应角色权限")
    @RequestMapping(value = "/permission/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long roleId) {
        List<UmsPermission> permissionList =roleService.getPermissionList(roleId);
        return new Response().success(permissionList);
    }

    @ApiOperation("修改角色权限")
    @RequestMapping(value = "/permission/update", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePermission(@RequestParam Long roleId,
                                   @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = roleService.updatePermission(roleId,permissionIds);
        if(count>0){
            return new Response().success(count);
        }
        return new Response().failure();
    }

    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Object list(){
        List<UmsRole> roleList = roleService.list();
        return new Response().success(roleList);
    }

}
