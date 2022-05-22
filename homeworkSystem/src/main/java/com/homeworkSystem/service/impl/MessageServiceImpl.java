package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.entity.Message;
import com.homeworkSystem.mapper.MessageMapper;
import com.homeworkSystem.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public void create(String message, Long uid) {
        Message newMessage=new Message();
        newMessage.setContent(message);
        if(uid!=null){
            newMessage.setUid(uid);
        }else{
            newMessage.setUid(1L);
        }
        newMessage.setTime(Math.random() * 10 + 3);
        baseMapper.insert(newMessage);
    }

    @Override
    public List<MessageVo> getMessage() {
        List<MessageVo>messages=baseMapper.getMessage();
        return messages;
    }

    @Override
    public IPage<MessageVo> queryByPage(int currPage, int pageSize,String message) {
        IPage<Message>page=new Page<>(currPage,pageSize);
        LambdaQueryWrapper<Message>wrapper=new LambdaQueryWrapper<>();
        if(message!=null){
            wrapper.like(Message::getContent,message);
        }
        return baseMapper.queryByPage(page,wrapper);
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Message>().between(Message::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }
}
