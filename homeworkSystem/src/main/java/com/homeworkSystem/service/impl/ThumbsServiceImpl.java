package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.entity.Thumbs;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.mapper.ThumbsMapper;
import com.homeworkSystem.service.ThumbsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.stereotype.Service;

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
public class ThumbsServiceImpl extends ServiceImpl<ThumbsMapper, Thumbs> implements ThumbsService {

    @Override
    public Boolean create(Long uid, Long bid) {
        LambdaQueryWrapper<Thumbs>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Thumbs::getUid,uid).eq(Thumbs::getBlogId,bid);
        Thumbs thumbs=baseMapper.selectOne(wrapper1);
        if(thumbs==null) {
            thumbs=new Thumbs();
            thumbs.setUid(uid);
            thumbs.setBlogId(bid);
            baseMapper.insert(thumbs);
            return true;
        }else {
            throw new MyException(ResultCode.ThumbsAlready);
        }
    }

    @Override
    public Boolean query(Long uid, Long bid) {
        LambdaQueryWrapper<Thumbs>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Thumbs::getUid,uid).eq(Thumbs::getBlogId,bid);
        Thumbs thumbs=baseMapper.selectOne(wrapper1);
        if(thumbs==null) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void deleteByBlogId(Long bid) {
        LambdaQueryWrapper<Thumbs>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Thumbs::getBlogId,bid);
        baseMapper.delete(wrapper);
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Thumbs>().between(Thumbs::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }

    @Override
    public void delete(Long uid, Long bid) {
        LambdaQueryWrapper<Thumbs>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Thumbs::getUid,uid).eq(Thumbs::getBlogId,bid);
        baseMapper.delete(wrapper1);
    }
}
