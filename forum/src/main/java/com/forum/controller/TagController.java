package com.forum.controller;


import com.forum.entity.Tag;
import com.forum.entity.Type;
import com.forum.service.TagService;
import com.forum.service.TypeService;
import com.forum.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/forum/tag")
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

