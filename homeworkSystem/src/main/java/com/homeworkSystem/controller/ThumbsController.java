package com.homeworkSystem.controller;


import com.homeworkSystem.service.BlogService;
import com.homeworkSystem.service.ThumbsService;
import com.homeworkSystem.service.UserService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hs/thumbs")
@Api(tags="Thumbs")
public class ThumbsController {

    @Autowired
    ThumbsService thumbsService;
    @Autowired
    BlogService blogService;
    @Autowired
    UserService userService;

    @ApiOperation("点赞")
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult create(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        Boolean flag=thumbsService.create(uid,bid);
        if(flag) {
            blogService.updateThumbs(bid);
            userService.sendBlogMessage(bid,"您的帖子收到一个点赞！");
        }
        return CommonResult.ok();
    }

    @ApiOperation("是否点赞")
    @GetMapping("/query")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult query(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        Boolean flag=thumbsService.query(uid,bid);
        return CommonResult.ok().data(flag);
    }

    @ApiOperation("取消点赞")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult delete(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        thumbsService.delete(uid,bid);
        blogService.updateThumbs(bid);
        return CommonResult.ok();
    }

}

