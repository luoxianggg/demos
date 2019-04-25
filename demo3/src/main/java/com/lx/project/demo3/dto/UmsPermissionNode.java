package com.lx.project.demo3.dto;

import com.lx.project.demo3.model.sys.UmsPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by macro on 2018/9/30.
 */
public class UmsPermissionNode extends UmsPermission {
/*    @Getter
    @Setter*/

    public List<UmsPermissionNode> getChildren() {
        return children;
    }

    public void setChildren(List<UmsPermissionNode> children) {
        this.children = children;
    }

    private List<UmsPermissionNode> children;
}
