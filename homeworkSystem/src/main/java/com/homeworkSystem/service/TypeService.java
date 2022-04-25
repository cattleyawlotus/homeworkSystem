package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface TypeService extends IService<Type> {

    void addType(String typeName);

    void deleteType(Long id);

    void updateType(Long id, String typeName);

    IPage<Type> queryByPage(int currPage, int pageSize,String typeName);

}
