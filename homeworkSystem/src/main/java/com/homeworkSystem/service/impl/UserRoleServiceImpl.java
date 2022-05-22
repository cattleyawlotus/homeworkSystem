package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.entity.Role;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.entity.UserRole;
import com.homeworkSystem.mapper.UserRoleMapper;
import com.homeworkSystem.service.RoleService;
import com.homeworkSystem.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Override
    public void createRelation(String userName) {
        LambdaQueryWrapper<User>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,userName);
        User user=userService.getOne(wrapper);

        String roles[]=user.getRole().split(",");
        LambdaQueryWrapper<Role> wrapper2=new LambdaQueryWrapper<>();
        wrapper2.in(Role::getRoleName,roles);
        List<Role> rolesList=roleService.list(wrapper2);
        for(Role role:rolesList)
        {
            UserRole userRole=new UserRole();
            userRole.setRoleId(role.getId());
            userRole.setUserId(user.getId());
            userRole.setIsDeleted(0);
            baseMapper.insert(userRole);
        }


    }

    @Override
    public void delete(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,id));
    }
}
