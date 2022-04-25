package com.homeworkSystem.security.security;

import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1.从header里面获取token
        String token = request.getHeader("token");
        if(token==null)
        {
            if(request.getHeader("Authorization").split(" ").length==1)
            {
                token = request.getHeader("Authorization").split(" ")[0];
            }else
            {
                token = request.getHeader("Authorization").split(" ")[1];
            }
        }
        //2.token不为空，移除token，从redis删除
        if (token != null) {
            tokenManager.removeToken(token);
            //清空当前用户缓存中的权限数据
            String userName = tokenManager.getUserFromToken(token);
            redisTemplate.delete(userName);
            redisTemplate.delete(userName+"_id");
        }
        ResponseUtil.out(response, CommonResult.ok().message("退出成功"));
    }
}
