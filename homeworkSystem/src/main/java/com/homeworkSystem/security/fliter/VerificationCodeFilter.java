package com.homeworkSystem.security.fliter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.security.wrapper.RepeatedlyReadRequestWrapper;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResponseUtil;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码验证
 */
public class VerificationCodeFilter extends OncePerRequestFilter {

//    private AuthenticationFailureHandler failureHandler;

    private RedisTemplate redisTemplate;

    public VerificationCodeFilter(RedisTemplate redisTemplate) {
//        this.failureHandler = failureHandler;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().indexOf("login") == -1) {
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("================="+request.getRequestURI());
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RepeatedlyReadRequestWrapper(request);
        }
        User user = new ObjectMapper().readValue(requestWrapper.getInputStream(), User.class);
        String captcha = user.getCode();
        String cToken = user.getVerKey();
        String expect= (String) redisTemplate.opsForValue().get(cToken);
        logger.info("code================="+captcha);
        logger.info("expect================="+expect);
        if(captcha.equals(expect)){
            filterChain.doFilter(requestWrapper, response);
        }else{
            ResponseUtil.out(response, CommonResult.error().code(ResultCode.VerificationError));
        }
    }
}



