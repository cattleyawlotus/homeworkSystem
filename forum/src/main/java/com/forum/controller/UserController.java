package com.forum.controller;


import com.forum.Vo.RegisterVo;
import com.forum.entity.User;
import com.forum.security.security.TokenManager;
import com.forum.service.UserService;
import com.forum.utils.CommonResult;
import com.forum.utils.MD5;
import com.forum.utils.ResultCode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/forum/user")
@Api(tags="User")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private DefaultKaptcha producer;

//    @ApiOperation("用户登录")
//    @PostMapping("/login")
//    public CommonResult login(@RequestBody User user){
//        String token=userService.login(user);
//        return CommonResult.ok();
//    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterVo user){
        userService.register(user);
        return CommonResult.ok().data(user);
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
    public CommonResult info(@RequestParam(value = "uid")Long uid){
        User user=userService.getById(uid);
        if(user==null){
            return CommonResult.error().code(ResultCode.UserNotExist);
        }
        if(redisTemplate.opsForValue().get(user.getUsername()+"_id").toString().equals(uid.toString())){
            user.setPassword("******");
            return CommonResult.ok().data(user);
        }else{
            return  CommonResult.error().code(ResultCode.Forbidden);
        }
    }

    @ApiOperation("修改个人信息")
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
    public CommonResult info(@RequestBody User user){
         user.setPassword(MD5.encrypt(user.getPassword()));
         userService.updateById(user);
        return  CommonResult.ok();
    }

    @ApiOperation("test")
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
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

}

