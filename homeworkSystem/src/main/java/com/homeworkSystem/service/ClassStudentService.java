package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.Blog;
import com.homeworkSystem.entity.ClassStudent;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ClassStudentService extends IService<ClassStudent> {

    IPage<ClassStudent> getStudents(int currPage, int pageSize, Long cid);
}
