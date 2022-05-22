package com.homeworkSystem.service.impl;

import com.homeworkSystem.entity.Role;
import com.homeworkSystem.mapper.RoleMapper;
import com.homeworkSystem.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
