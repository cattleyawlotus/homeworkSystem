package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.Vo.BlogVo;
import com.homeworkSystem.Vo.PageReq;
import com.homeworkSystem.entity.*;
import com.homeworkSystem.service.*;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/hs/blog")
@Api(tags="Blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    @Autowired
    UserService userService;
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
    CommentService commentService;

    //<editor-fold desc="博客">
    @ApiOperation("新建博客")
    @PostMapping("/createblog")
    public CommonResult createBlog(@RequestBody BlogVo blogVo){
         Blog blog=new Blog();
         BeanUtils.copyProperties(blogVo,blog);
         blogService.save(blog);
         blogTagsService.createRelation(blogVo.getTags(),blog.getTitle());
        return CommonResult.ok();
    }

    @ApiOperation("删除博客")
    @DeleteMapping("/deleteblog")
    public CommonResult deleteBlog(@RequestParam(value = "bid")Long bid,@RequestParam(value = "uid")Long uid){
        if(blogService.delete(bid,uid)){
            blogTagsService.deleteByBlogId(bid);
            thumbsService.deleteByBlogId(bid);
            viewsService.deleteByBlogId(bid);
            collectsService.deleteByBlogId(bid);
            commentService.deleteByBlogId(bid);
        }
        return CommonResult.ok();
    }

    @ApiOperation("修改博客")
    @PutMapping("/updateblog")
    public CommonResult updateBlog(@RequestBody BlogVo blogVo){
        Blog blog=new Blog();
        BeanUtils.copyProperties(blogVo,blog);
        blogService.updateById(blog);
        blogTagsService.deleteByBlogId(blogVo.getId());
        blogTagsService.createRelation(blogVo.getTags(),blog.getTitle());
        return CommonResult.ok();
    }

    @ApiOperation("分页查询博客")
    @GetMapping("/getallblog")
    public CommonResult getAllBlog(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String title){
        PageUtil<Blog>page = new PageUtil<>(blogService.getAllBlog(req.getCurrPage(),req.getPageSize(),title));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }

    @ApiOperation("查询最新推荐博客（10条）")
    @GetMapping("/getlatestblog")
    public CommonResult getLatestBlog(@RequestParam(value = "hours",defaultValue="24")long hours){
        List<Blog>blogList = blogService.getLatestBlog(hours);
        List<BlogVo> list=blogList.stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4= new LambdaQueryWrapper<>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());
        return CommonResult.ok().data(list);
    }

    @ApiOperation("根据id查询博客")
    @GetMapping("/getblogbyid")
    public CommonResult getBlogById(@RequestParam(value = "id")Long id){
        Blog blog = blogService.getBlogById(id);
        BlogVo blogVo=new BlogVo();
        BeanUtils.copyProperties(blog,blogVo);
        LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(User::getId,blog.getUid());
        User user=userService.getOne(wrapper1);
        if(user!=null) {
            blogVo.setUsername(user.getUsername());
            blogVo.setAvatar(user.getAvatar());
        }
        LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
        wrapper2.eq(Type::getId,blog.getTypeId());
        Type type=typeService.getOne(wrapper2);
        if(type!=null) {
            blogVo.setType(type.getTypeName());
        }
        LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
        wrapper3.eq(BlogTags::getBlogId,id);
        List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
        List<Tag>tagList=new ArrayList<>();
        for(BlogTags blogTags:blogTagsList){
            LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
            wrapper4.eq(Tag::getId,blogTags.getTagId());
            tagList.add(tagService.getOne(wrapper4));
        }
        blogVo.setTags(tagList);
        return CommonResult.ok().data(blogVo);
    }

    @ApiOperation("分页查询用户博客")
    @GetMapping("/getuserblog")
    public CommonResult getUserBlog(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(value = "uid")long uid){
        PageUtil<Blog>page = new PageUtil<>(blogService.getUserBlog(req.getCurrPage(),req.getPageSize(),uid));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }

    //</editor-fold >

    //<editor-fold desc="type">
    @ApiOperation("按type名分页查询最新博客")
    @GetMapping("/getblogbytype")
    public CommonResult getBlogByType(@RequestParam(value = "type")String type,@Validated @ApiParam("分页参数") PageReq req){
        PageUtil<Blog>page = new PageUtil<>(blogService.getBlogByType(type,req.getCurrPage(),req.getPageSize()));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            if(type!=null) {
                blogVo.setType(type);
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }

    @ApiOperation("按typeid分页查询最新博客")
    @GetMapping("/getblogbytypeid")
    public CommonResult getBlogByTypeId(@RequestParam(value = "id")Long id,@Validated @ApiParam("分页参数") PageReq req){
        PageUtil<Blog>page = new PageUtil<>(blogService.getBlogByTypeId(id,req.getCurrPage(),req.getPageSize()));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }
    //</editor-fold >

    //<editor-fold desc="tag">
    @ApiOperation("按tag名分页查询最新博客")
    @GetMapping("/getblogbytag")
    public CommonResult getBlogByTag(@RequestParam(value = "tag")String tag,@Validated @ApiParam("分页参数") PageReq req){
        PageUtil<Blog>page = new PageUtil<>(blogService.getBlogByTag(tag,req.getCurrPage(),req.getPageSize()));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }

    @ApiOperation("按tagid分页查询最新博客")
    @GetMapping("/getblogbytagid")
    public CommonResult getBlogByTagId(@RequestParam(value = "id")Long id,@Validated @ApiParam("分页参数") PageReq req){
        PageUtil<Blog>page = new PageUtil<>(blogService.getBlogByTagId(id,req.getCurrPage(),req.getPageSize()));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User>wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());

        PageUtil<BlogVo> res=new PageUtil<>(list,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }
    //</editor-fold >






}

