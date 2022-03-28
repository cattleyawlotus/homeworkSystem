package com.forum.service;

import com.forum.entity.User;
import com.forum.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface UserRoleService extends IService<UserRole> {
    void createRelation(String userName);

    void delete(Long id);
}
