package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;


public interface TagService extends IService<Tag> {

    void addTag(String tagName);

    void deleteTag(Long id);

    void updateTag(Long id, String tagName);

    IPage<Tag>  queryByPage(int currPage, int pageSize,String tagName);

}
