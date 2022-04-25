package com.homeworkSystem.controller;


import com.homeworkSystem.service.BlogService;
import com.homeworkSystem.service.ViewsService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hs/views")
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

