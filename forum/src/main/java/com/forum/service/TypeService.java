package com.forum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.forum.entity.Blog;
import com.forum.entity.Type;
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

    IPage<Type> queryByPage(int currPage, int pageSize);
}
