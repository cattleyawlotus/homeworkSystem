package com.forum.service;

import com.forum.entity.Blog;
import com.forum.entity.BlogTags;
import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.Tag;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface BlogTagsService extends IService<BlogTags> {

    void createRelation(List<Tag> tags, String title);

    void deleteByBlogId(Long bid);
}
