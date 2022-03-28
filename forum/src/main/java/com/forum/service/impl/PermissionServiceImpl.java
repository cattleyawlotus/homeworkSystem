package com.forum.service.impl;

import com.forum.entity.Permission;
import com.forum.mapper.PermissionMapper;
import com.forum.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<String> selectPermissionValueByUserId(Long id) {
        List<String> selectPermissionValueList = null;
//        if(this.isSysAdmin(id)) {
//            //如果是系统管理员，获取所有权限
//            selectPermissionValueList = baseMapper.selectAllPermissionValue();
//        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id.toString());
//        }
        return selectPermissionValueList;
    }
}
