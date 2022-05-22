package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.entity.Homework;
import com.homeworkSystem.mapper.HomeworkMapper;
import com.homeworkSystem.service.HomeworkService;
import org.springframework.stereotype.Service;



@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {

}