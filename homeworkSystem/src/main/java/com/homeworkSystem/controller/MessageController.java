package com.homeworkSystem.controller;


import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.service.MessageService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hs/message")
@Api(tags="Message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @ApiOperation("新建留言")
    @PostMapping("/create")
    public CommonResult create(@RequestParam(value = "message")String message,@RequestParam(value = "uid",required = false)Long uid){
        messageService.create(message,uid);
        return CommonResult.ok();
    }

    @ApiOperation("获取留言")
    @GetMapping("/getmessage")
    public CommonResult getMessage(){
        List<MessageVo>messages= messageService.getMessage();
        return CommonResult.ok().data(messages);
    }


}

