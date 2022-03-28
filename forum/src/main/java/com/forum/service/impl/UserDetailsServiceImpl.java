package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.forum.entity.Role;
import com.forum.entity.SecurityUser;
import com.forum.entity.User;
import com.forum.entity.UserRole;
import com.forum.service.PermissionService;
import com.forum.service.RoleService;
import com.forum.service.UserRoleService;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询数据
        User user=userService.selectByUsername(username);
        //判断
        if(user==null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        //根据用户查询权限
        List<String> permissionValueList= new ArrayList<>();
//        List<String> permissionValueList=permissionService.selectPermissionValueByUserId(user.getId().toString());
        LambdaQueryWrapper<UserRole>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(UserRole::getUserId,user.getId());
        List<UserRole> userRoles=userRoleService.list(wrapper1);
        for(UserRole userRole:userRoles)
        {
            Role role=roleService.getById(userRole.getRoleId());
            if(role!=null)
            {
                permissionValueList.add("ROLE_"+role.getRoleName());
            }
        }
        SecurityUser securityUser=new SecurityUser(user);
        securityUser.setPermissionValueList(permissionValueList);
        return  securityUser;
    }
}
