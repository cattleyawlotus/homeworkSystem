package com.homeworkSystem.controller;


import com.homeworkSystem.entity.Type;
import com.homeworkSystem.service.TypeService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/hs/type")
@Api(tags="Type")
public class TypeController {

    @Autowired
    TypeService typeService;
    @ApiOperation("获取所有类型")
    @GetMapping("/getalltype")
    public CommonResult getAllType() {
        List<Type> typeList=typeService.list();
        return CommonResult.ok().data(typeList);
    }

}

