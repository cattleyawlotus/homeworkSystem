package com.homeworkSystem.service;

import com.homeworkSystem.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserRoleService extends IService<UserRole> {
    void createRelation(String userName);

    void delete(Long id);
}
