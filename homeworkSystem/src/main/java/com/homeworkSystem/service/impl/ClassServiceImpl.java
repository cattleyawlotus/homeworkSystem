package com.homeworkSystem.service.impl;

import com.homeworkSystem.entity.Class;
import com.homeworkSystem.mapper.ClassMapper;
import com.homeworkSystem.service.ClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {

}
