package com.forum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.forum.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.Type;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface TagService extends IService<Tag> {

    void addTag(String tagName);

    void deleteTag(Long id);

    IPage<Tag>  queryByPage(int currPage, int pageSize);
}
