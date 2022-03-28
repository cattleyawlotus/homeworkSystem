package com.forum.service;

import com.forum.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface PermissionService extends IService<Permission> {

    List<String> selectPermissionValueByUserId(Long id);
}
