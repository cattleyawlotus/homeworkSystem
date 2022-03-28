package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.Vo.RegisterVo;
import com.forum.entity.Blog;
import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.security.security.DefaultPasswordEncoder;
import com.forum.security.security.TokenManager;
import com.forum.service.UserRoleService;
import com.forum.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.handler.exceptionhandler.MyException;
import com.forum.utils.MD5;
import com.forum.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;
    @Override
    public User selectByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    @Override
    public String login(User user) {
        String password=user.getPassword();
        if(StringUtils.isEmpty(password))
        {
            throw new MyException(ResultCode.UserLoginVerifyFail);
        }
        QueryWrapper<User>wrapper=new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User newUser=baseMapper.selectOne(wrapper);
        if(newUser==null)
        {
            throw new MyException(ResultCode.UserNotExist);
        }
        if(!new DefaultPasswordEncoder().matches(password,newUser.getPassword()))
        {
            throw new MyException(ResultCode.UserLoginVerifyFail);
        }
        String token=TokenManager.createToken(newUser.getUsername());
        return token;
    }

    @Override
    public void register(RegisterVo user) {
        if(StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())||StringUtils.isEmpty(user.getRole()))
        {
            throw new MyException(ResultCode.AddUserFail);
        }

        LambdaQueryWrapper<User>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        Integer count=baseMapper.selectCount(wrapper);
        if(count>0)
        {
            throw  new MyException(ResultCode.UserAlreadyExist);
        }
        User newUser=new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(MD5.encrypt(user.getPassword()));
        newUser.setRole(user.getRole());
        newUser.setAvatar(user.getAvatar());
        newUser.setNickname(user.getNickname());
        newUser.setEmail(user.getEmail());
        baseMapper.insert(newUser);
        userRoleService.createRelation(newUser.getUsername());
    }

    @Override
    public void delete(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<User>().eq(User::getId,id));
        userRoleService.delete(id);
    }

    @Override
    public IPage<User> queryByPage(int currPage, int pageSize) {
        IPage<User> res=baseMapper.selectPage(new Page<>(currPage,pageSize),new LambdaQueryWrapper<>());
        return res;
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<User>().between(User::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }
}
