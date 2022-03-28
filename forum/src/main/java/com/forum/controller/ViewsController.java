package com.forum.controller;


import com.forum.entity.Views;
import com.forum.service.BlogService;
import com.forum.service.ViewsService;
import com.forum.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/forum/views")
@Api(tags="Views")
public class ViewsController {
    @Autowired
    ViewsService viewsService;
    @Autowired
    BlogService blogService;

    @ApiOperation("浏览")
    @PostMapping("/create")
    public CommonResult create(@RequestParam(value = "bid")Long bid,@RequestParam(value = "ip")String ip){
        viewsService.create(bid,ip);
        blogService.updateViews(bid);
        return CommonResult.ok();
    }
}

