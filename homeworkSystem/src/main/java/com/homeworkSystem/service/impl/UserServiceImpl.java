package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.Vo.RegisterVo;
import com.homeworkSystem.entity.Blog;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.mapper.UserMapper;
import com.homeworkSystem.security.security.DefaultPasswordEncoder;
import com.homeworkSystem.security.security.TokenManager;
import com.homeworkSystem.service.BlogService;
import com.homeworkSystem.service.UserRoleService;
import com.homeworkSystem.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.utils.MD5;
import com.homeworkSystem.utils.ResultCode;
import com.homeworkSystem.websocket.WebSocketServer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeoutException;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;
    @Autowired
    BlogService blogService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;

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
    public IPage<User> queryByPage(int currPage, int pageSize,String userName) {
       LambdaQueryWrapper<User>wrapper= new LambdaQueryWrapper<>();
       if(userName!=null){
           wrapper.like(User::getUsername,userName);
       }
        IPage<User> res=baseMapper.selectPage(new Page<>(currPage,pageSize),wrapper);
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

    @Override
    public void sendBlogMessage(Long bid, String message) {
        Blog blog=blogService.getBlogById(bid);
        boolean flag=false;
        if(blog!=null){
            try {
                flag=WebSocketServer.sendInfo(message,blog.getUid().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(true){
                String qname="queue"+blog.getUid();
                String exchange="exchange"+blog.getUid();
                String key=blog.getUid().toString();
                String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                message+="博客名为《"+blog.getTitle()+"》";
                message=createTime+" "+message;
                Properties p=amqpAdmin.getQueueProperties(qname);
                if(p==null){
                    amqpAdmin.declareExchange(new DirectExchange(exchange));
                    Queue queue=new Queue(qname,true,false,true,null);
                    amqpAdmin.declareQueue(queue);
                    amqpAdmin.declareBinding(new Binding(qname, Binding.DestinationType.QUEUE,exchange,key,null));
                }
                MessageProperties messageProperties = new MessageProperties();
                messageProperties.setContentType("application/json");
                Message me = new Message(message.getBytes(StandardCharsets.UTF_8),messageProperties);
                rabbitTemplate.convertAndSend(exchange,key,me);
            }
        }
    }



    @Override
    public int sendOffLineMessage(String uid) {
        String qname="queue"+uid;
        if(amqpAdmin.getQueueProperties(qname)==null){
            return 0;
        }
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            Channel channel = connection.createChannel();
            AMQP.Queue.DeclareOk declareOk=channel.queueDeclarePassive(qname);
            return  declareOk.getMessageCount();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
