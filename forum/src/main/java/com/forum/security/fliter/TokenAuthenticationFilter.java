package com.forum.security.fliter;

import com.forum.security.security.TokenManager;
import com.forum.utils.CommonResult;
import com.forum.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public TokenAuthenticationFilter(AuthenticationManager authManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {


        logger.info("================="+req.getRequestURI());
        if(req.getRequestURI().indexOf("admin") != -1) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            //获取当前认证成功用户权限信息
            authentication = getAuthentication(req);
        } catch (Exception e) {
            ResponseUtil.out(res, CommonResult.error());
        }
        //判断如果有权限信息，放到权限上下文
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ResponseUtil.out(res, CommonResult.error());
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //从header获取token
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
        logger.info("================token "+token);
        if (token != null && !"".equals(token.trim())) {
            //从token获取用户名
            String userName = tokenManager.getUserFromToken(token);
            //从redis获取对应权限列表
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(userName);
            //变成 Collection<? extends GrantedAuthority> authorities
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for(String permissionValue : permissionValueList) {
                if(StringUtils.isEmpty(permissionValue)) continue;
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
                authorities.add(authority);
            }

            if (!StringUtils.isEmpty(userName)) {
                return new UsernamePasswordAuthenticationToken(userName, token, authorities);
            }
            return null;
        }
        return null;
    }
}
