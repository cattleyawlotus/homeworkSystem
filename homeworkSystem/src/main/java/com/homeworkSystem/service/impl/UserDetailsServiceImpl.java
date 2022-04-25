package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.entity.Role;
import com.homeworkSystem.entity.SecurityUser;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.entity.UserRole;
import com.homeworkSystem.service.RoleService;
import com.homeworkSystem.service.UserRoleService;
import com.homeworkSystem.service.UserService;
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
