package com.homeworkSystem.controller;


import com.homeworkSystem.entity.Tag;
import com.homeworkSystem.service.TagService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/hs/tag")
@Api(tags="Type")
public class TagController {
    @Autowired
    TagService tagService;

    @ApiOperation("获取所有标签")
    @GetMapping("/getalltag")
    public CommonResult getAllTag() {
        List<Tag> tagList=tagService.list();
        return CommonResult.ok().data(tagList);
    }



}

