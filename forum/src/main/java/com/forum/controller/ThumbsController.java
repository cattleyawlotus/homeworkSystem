package com.forum.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.forum.Vo.BlogVo;
import com.forum.Vo.PageReq;
import com.forum.entity.Blog;
import com.forum.entity.Type;
import com.forum.entity.User;
import com.forum.service.BlogService;
import com.forum.service.ThumbsService;
import com.forum.utils.CommonResult;
import com.forum.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/forum/thumbs")
@Api(tags="Thumbs")
public class ThumbsController {

    @Autowired
    ThumbsService thumbsService;
    @Autowired
    BlogService blogService;

    @ApiOperation("点赞")
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_user')")
    public CommonResult create(@RequestParam(value = "uid")Long uid,@RequestParam(value = "bid")Long bid){
        Boolean flag=thumbsService.create(uid,bid);
        if(flag) {
            blogService.updateThumbs(bid);
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

