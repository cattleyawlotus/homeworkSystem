package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.entity.Blog;
import com.homeworkSystem.entity.ClassStudent;
import com.homeworkSystem.mapper.ClassStudentMapper;
import com.homeworkSystem.service.ClassStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.utils.PageUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-04-25
 */
@Service
public class ClassStudentServiceImpl extends ServiceImpl<ClassStudentMapper, ClassStudent> implements ClassStudentService {

    @Override
    public IPage<ClassStudent> getStudents(int currPage, int pageSize, Long cid) {
        IPage<ClassStudent>page=new Page<>(currPage,pageSize);
        LambdaQueryWrapper<ClassStudent> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ClassStudent::getClassId,cid);
        IPage<ClassStudent>res=baseMapper.selectPage(page,wrapper);
        return res;
    }
}
