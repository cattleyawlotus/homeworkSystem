package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.entity.Collects;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.mapper.CollectsMapper;
import com.homeworkSystem.service.CollectsService;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CollectsServiceImpl extends ServiceImpl<CollectsMapper, Collects> implements CollectsService {

    @Override
    public Boolean create(Long uid, Long bid) {
        LambdaQueryWrapper<Collects> wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Collects::getUid,uid).eq(Collects::getBlogId,bid);
        Collects collects=baseMapper.selectOne(wrapper1);
        if(collects==null) {
            collects=new Collects();
            collects.setUid(uid);
            collects.setBlogId(bid);
            baseMapper.insert(collects);
            return true;
        }else {
            throw new MyException(ResultCode.CollectsAlready);
        }
    }

    @Override
    public Boolean query(Long uid, Long bid) {
        LambdaQueryWrapper<Collects>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Collects::getUid,uid).eq(Collects::getBlogId,bid);
        Collects collects=baseMapper.selectOne(wrapper1);
        if(collects==null) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void deleteByBlogId(Long bid) {
        LambdaQueryWrapper<Collects>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Collects::getBlogId,bid);
        baseMapper.delete(wrapper);
    }

    @Override
    public void delete(Long uid, Long bid) {
        LambdaQueryWrapper<Collects>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Collects::getUid,uid).eq(Collects::getBlogId,bid);
        baseMapper.delete(wrapper1);
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Collects>().between(Collects::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }
}