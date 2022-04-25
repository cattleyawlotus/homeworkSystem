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
@RequestMapping("/forum/collects")
@Api(tags="Collects")
public class CollectsController {

    @Autowired
    BlogService blogService;
    @Autowired
    CollectsService collectsService;
    @Autowired
    UserService userService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogTagsService blogTagsService;

    @ApiOperation("收藏")
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult create(@RequestParam(value = "uid")Long uid, @RequestParam(value = "bid")Long bid){
        Boolean flag=collectsService.create(uid,bid);
        if(flag) {
            blogService.updateCollects(bid);
            userService.sendBlogMessage(bid,"您的帖子收到一个收藏！");
        }
        return CommonResult.ok();
    }

    @ApiOperation("是否收藏")
    @GetMapping("/query")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult query(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        Boolean flag=collectsService.query(uid,bid);
        return CommonResult.ok().data(flag);
    }

    @ApiOperation("取消收藏")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult delete(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        collectsService.delete(uid,bid);
        blogService.updateCollects(bid);
        return CommonResult.ok();
    }


    @ApiOperation("分页查询用户收藏的博客")
    @GetMapping("/getcollectedblog")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult getAllBlog(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(value = "uid")Long uid){
        PageUtil<Blog> page = new PageUtil<>(blogService.getCollectedBlog(req.getCurrPage(),req.getPageSize(),uid));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User> wrapper1=new LambdaQueryWrapper<>();
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



}