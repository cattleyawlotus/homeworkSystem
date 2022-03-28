package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.forum.entity.Blog;
import com.forum.entity.BlogTags;
import com.forum.entity.Tag;
import com.forum.mapper.BlogTagsMapper;
import com.forum.service.BlogService;
import com.forum.service.BlogTagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@Service
public class BlogTagsServiceImpl extends ServiceImpl<BlogTagsMapper, BlogTags> implements BlogTagsService {
    @Autowired
    BlogService blogService;

    @Override
    public void createRelation(List<Tag> tags, String title) {
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getTitle,title);
        Blog blog=blogService.getOne(wrapper);
        if(blog!=null){
            for(Tag tag:tags){
                BlogTags blogTags=new BlogTags();
                blogTags.setBlogId(blog.getId());
                blogTags.setTagId(tag.getId());
                baseMapper.insert(blogTags);
            }
        }
    }

    @Override
    public void deleteByBlogId(Long bid) {
        LambdaQueryWrapper<BlogTags>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(BlogTags::getBlogId,bid);
        baseMapper.delete(wrapper);
    }
}
