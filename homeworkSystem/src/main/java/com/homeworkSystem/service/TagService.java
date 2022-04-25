package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

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

    void updateTag(Long id, String tagName);

    IPage<Tag>  queryByPage(int currPage, int pageSize,String tagName);

}
