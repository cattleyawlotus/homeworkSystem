package com.forum.controller;


import com.forum.Vo.BlogVo;
import com.forum.Vo.MessageVo;
import com.forum.entity.Blog;
import com.forum.service.MessageService;
import com.forum.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/forum/message")
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

