package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.entity.Blog;
import com.homeworkSystem.entity.ClassStudent;
import com.homeworkSystem.entity.Homework;
import com.homeworkSystem.mapper.HomeworkMapper;
import com.homeworkSystem.service.ClassStudentService;
import com.homeworkSystem.service.HomeworkService;
import com.homeworkSystem.websocket.WebSocketServer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;


@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {
    @Autowired
    ClassStudentService classStudentService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;

    @Override
    public void sendHomeworkMessage(Long cid, String message) {
        List<ClassStudent> classStudentList=classStudentService.list(new LambdaQueryWrapper<ClassStudent>().eq(ClassStudent::getClassId,cid));
        for(ClassStudent x:classStudentList){
            boolean flag=false;
            try {
                //如果用户在线，则直接发送消息
                flag= WebSocketServer.sendInfo(message,x.getStudentId().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果用户不在线，则发送消息到消息队列
            if(!flag){
                String qname="queue"+x.getStudentId();
                String exchange="exchange"+x.getStudentId();
                String key=x.getStudentId().toString();
                String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
}