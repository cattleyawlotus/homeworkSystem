package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.Vo.statistics.BlogData;
import com.forum.entity.*;
import com.forum.mapper.BlogMapper;
import com.forum.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogTagsService blogTagsService;
    @Autowired
    ThumbsService thumbsService;
    @Autowired
    ViewsService viewsService;
    @Autowired
    CollectsService collectsService;
    @Autowired
    UserService userService;

    @Override
    public IPage<Blog> getAllBlog(int curr, int size) {
        IPage<Blog>page=new Page<>(curr,size);
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Blog::getCreateTime);
        IPage<Blog> res=baseMapper.selectPage(page,wrapper);
        return res;
    }

    @Override
    public IPage<Blog> getUserBlog(int currPage, int pageSize, long uid) {
        IPage<Blog>page=new Page<>(currPage,pageSize);
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getUid,uid)
                .orderByDesc(Blog::getCreateTime);
        IPage<Blog> res=baseMapper.selectPage(page,wrapper);
        return res;
    }

    @Override
    public IPage<Blog> getBlogByType(String type, int curr, int size) {
        LambdaQueryWrapper<Type>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Type::getTypeName,type);
        Type type1=typeService.getOne(wrapper1);

        IPage<Blog>page=new Page<>(curr,size);
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getTypeId,type1.getId())
                .orderByDesc(Blog::getCreateTime);
        IPage<Blog> res=baseMapper.selectPage(page,wrapper);
        return res;
    }

    @Override
    public IPage<Blog> getBlogByTypeId(Long id, int curr, int size) {
        IPage<Blog>page=new Page<>(curr,size);
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getTypeId,id)
                .orderByDesc(Blog::getCreateTime);
        IPage<Blog> res=baseMapper.selectPage(page,wrapper);
        return res;
    }

    @Override
    public IPage<Blog> getBlogByTag(String tag, int curr, int size) {
        LambdaQueryWrapper<Tag>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Tag::getTagName,tag);
        Tag tag1=tagService.getOne(wrapper1);

        LambdaQueryWrapper<BlogTags>wrapper2=new LambdaQueryWrapper<>();
        wrapper2.eq(BlogTags::getTagId,tag1.getId());
        List<BlogTags> blogTagsList=blogTagsService.list(wrapper2);

        List<Long>ids=new ArrayList<>();
        for(BlogTags blogTags:blogTagsList) {
            ids.add(blogTags.getBlogId());
        }

        IPage<Blog>page=new Page<>(curr,size);
        LambdaQueryWrapper<Blog>wrapper3=new LambdaQueryWrapper<>();
        wrapper3.in(Blog::getId,ids)
                .orderByDesc(Blog::getCreateTime);
        IPage<Blog> res=baseMapper.selectPage(page,wrapper3);
        return res;
    }

    @Override
    public IPage<Blog> getBlogByTagId(Long id, int curr, int size) {
        LambdaQueryWrapper<BlogTags>wrapper2=new LambdaQueryWrapper<>();
        wrapper2.eq(BlogTags::getTagId,id);
        List<BlogTags> blogTagsList=blogTagsService.list(wrapper2);

        List<Long>ids=new ArrayList<>();
        for(BlogTags blogTags:blogTagsList) {
            ids.add(blogTags.getBlogId());
        }
        if(ids.size()>0) {
            IPage<Blog>page=new Page<>(curr,size);
            LambdaQueryWrapper<Blog>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.in(Blog::getId,ids)
                    .orderByDesc(Blog::getCreateTime);
            IPage<Blog> res=baseMapper.selectPage(page,wrapper3);
            return res;
        }else {
            return new Page<>();
        }
    }

    @Override
    public IPage<Blog> getCollectedBlog(int currPage, int pageSize,Long uid) {
        LambdaQueryWrapper<Collects>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Collects::getUid,uid).orderByDesc(Collects::getCreateTime);
        List<Collects> collectsList=collectsService.list(wrapper);

        List<Long>ids=new ArrayList<>();
        for(Collects collects:collectsList) {
            ids.add(collects.getBlogId());
        }
        if(ids.size()>0) {
            IPage<Blog>page=new Page<>(currPage,pageSize);
            LambdaQueryWrapper<Blog>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.in(Blog::getId,ids);
            IPage<Blog> res=baseMapper.selectPage(page,wrapper2);
            return res;
        }else {
            return new Page<>();
        }
    }

    @Override
    public List<Blog> getLatestBlog(long hours) {
       QueryWrapper<Blog> wrapper=new QueryWrapper<>();
       LocalDateTime left=LocalDateTime.now().minusHours(hours);
       wrapper.between("create_time",left,LocalDateTime.now())
               .eq("recommend",1)
               .orderByDesc("create_time").last("limit 10");
       List<Blog>list=baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public Blog getBlogById(Long id) {
        Blog blog=baseMapper.selectById(id);
        return blog;
    }

    @Override
    public void updateThumbs(Long bid) {
        LambdaQueryWrapper<Thumbs>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Thumbs::getBlogId,bid);
        List<Thumbs> list= thumbsService.list(wrapper);

        Blog blog=baseMapper.selectById(bid);
        blog.setThumbs(String.valueOf(list.size()));
        baseMapper.updateById(blog);
    }

    @Override
    public void updateViews(Long bid) {
        LambdaQueryWrapper<Views>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Views::getBlogId,bid);
        List<Views> list= viewsService.list(wrapper);

        Blog blog=baseMapper.selectById(bid);
        blog.setViews(String.valueOf(list.size()));
        baseMapper.updateById(blog);
    }

    @Override
    public void updateCollects(Long bid) {
        LambdaQueryWrapper<Collects>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Collects::getBlogId,bid);
        List<Collects> list= collectsService.list(wrapper);

        Blog blog=baseMapper.selectById(bid);
        blog.setCollects(String.valueOf(list.size()));
        baseMapper.updateById(blog);
    }

    @Override
    public boolean delete(Long bid, Long uid) {
        User user=userService.getById(uid);
        LambdaQueryWrapper<Blog>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getId,bid);
        if(user!=null){
            if(user.getRole().equals("admin")){
                baseMapper.delete(wrapper);
            }else{
                wrapper.eq(Blog::getUid,uid);
                baseMapper.delete(wrapper);
            }
        }
        return true;
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Blog>().between(Blog::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }

    @Override
    public List<BlogData> getblogdata() {
        List<BlogData> data=baseMapper.getblogdata();
        return data;
    }
}
