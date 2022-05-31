package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.entity.HomeworkStudent;
import com.homeworkSystem.mapper.HomeworkStudentMapper;
import com.homeworkSystem.service.HomeworkStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-05-21
 */
@Service
public class HomeworkStudentServiceImpl extends ServiceImpl<HomeworkStudentMapper, HomeworkStudent> implements HomeworkStudentService {

    @Override
    public IPage<HomeworkStudent> getHomework(int currPage, int pageSize, Long hid) {
        IPage<HomeworkStudent>page=new Page<>(currPage,pageSize);
        LambdaQueryWrapper<HomeworkStudent>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(HomeworkStudent::getHomeworkId,hid);
        IPage<HomeworkStudent>res=baseMapper.selectPage(page,wrapper);
        return res;

    }
}
