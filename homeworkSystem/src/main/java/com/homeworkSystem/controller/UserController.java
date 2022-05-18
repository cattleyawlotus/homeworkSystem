package com.homeworkSystem.controller;


import com.homeworkSystem.Vo.RegisterVo;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.security.security.TokenManager;
import com.homeworkSystem.service.UserService;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResultCode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/hs/user")
@Api(tags="User")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private DefaultKaptcha producer;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @ApiOperation("注册")
    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterVo user){
        userService.register(user);
        return CommonResult.ok().data(user);
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/info")
    public CommonResult info(@RequestParam(value = "uid")Long uid){
        User user=userService.getById(uid);
        if(user==null){
            return CommonResult.error().code(ResultCode.UserNotExist);
        }
        if(redisTemplate.opsForValue().get(user.getUsername()+"_id").toString().equals(uid.toString())){

            return CommonResult.ok().data(user);
        }else{
            return  CommonResult.error().code(ResultCode.Forbidden);
        }
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    public CommonResult info(@RequestBody User user){
//         user.setPassword(MD5.encrypt(user.getPassword()));
         userService.updateById(user);
        return  CommonResult.ok();
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public CommonResult test(){
        return   CommonResult.ok().data(redisTemplate.opsForValue().get("uid"));
    }



    @ApiOperation(value = "获取验证码",notes= "获取验证码的接口")
    @GetMapping(value = "/default")
    public CommonResult generateVerificationCode() throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        ByteArrayOutputStream outputStream = null;
        BufferedImage image = producer.createImage(text);
        outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        map.put("img", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        String codeToken = TokenManager.createToken(text);
        redisTemplate.opsForValue().set(codeToken, text, 5, TimeUnit.MINUTES);
        map.put("cToken", codeToken);
        return CommonResult.ok().data(map);
    }

    @ApiOperation(value = "获取消息通知")
    @GetMapping("/receiveDirectMessage")
    public CommonResult receiveDirectMessage(@RequestParam String qname) {
        Message message=rabbitTemplate.receive(qname);
        List<byte[]> res=new ArrayList<>();
        while(message!=null){
            res.add(message.getBody());
            message=rabbitTemplate.receive(qname);
        }
        return CommonResult.ok().data(res);
    }



}

