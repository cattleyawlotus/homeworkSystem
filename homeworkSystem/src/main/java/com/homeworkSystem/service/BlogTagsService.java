package com.homeworkSystem.service;

import com.homeworkSystem.entity.BlogTags;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homeworkSystem.entity.Tag;

import java.util.List;


public interface BlogTagsService extends IService<BlogTags> {

    void createRelation(List<Tag> tags, String title);

    void deleteByBlogId(Long bid);
}
