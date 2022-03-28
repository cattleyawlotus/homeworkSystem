package com.forum.mapper;

import com.forum.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<String> selectPermissionValueByUserId(String id);
}
