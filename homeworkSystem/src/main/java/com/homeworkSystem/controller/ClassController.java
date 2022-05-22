package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homeworkSystem.entity.*;
import com.homeworkSystem.entity.Class;
import com.homeworkSystem.minio.service.MinioService;
import com.homeworkSystem.service.ClassService;
import com.homeworkSystem.service.ClassStudentService;
import com.homeworkSystem.service.HomeworkService;
import com.homeworkSystem.service.HomeworkStudentService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hs/class")
public class ClassController {
    @Autowired
    ClassService classService;
    @Autowired
    ClassStudentService classStudentService;
    @Autowired
    HomeworkService homeworkService;
    @Autowired
    HomeworkStudentService homeworkStudentService;
    @Autowired
    MinioService minioService;

    @ApiOperation("新建班级")
    @PostMapping("/createclass")
    public CommonResult createClass(@RequestParam(value = "tid") Long tid,@RequestParam(value="className")String className){
        Class class1=new Class();
        class1.setName(className);
        class1.setTeacherId(tid);
        classService.save(class1);
        //为班级新建bucket 以id作为标识
        if(!minioService.bucketExists(class1.getId().toString())){
            minioService.createBucket(class1.getId().toString());
            //配置访问策略
            minioService.createBucketPolicy(new StringBuilder(""),class1.getId().toString());
        }
        return CommonResult.ok();
    }

    @ApiOperation("删除班级")
    @DeleteMapping("/delete")
    public CommonResult deleteClass(@ApiParam("班级id") @RequestParam(value = "id") Long id){
        classService.removeById(id);
        classStudentService.remove(new QueryWrapper<ClassStudent>().eq("class_id",id));
        //删除作业相关表项
        LambdaQueryWrapper<Homework>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Homework::getClassId,id);
        homeworkService.list(wrapper).forEach(x->{
            homeworkStudentService.remove((new QueryWrapper<HomeworkStudent>().eq("homework_id",x.getId())));
            homeworkService.removeById(x.getId());
        });
        //删除班级的bucket
        if(minioService.bucketExists(id.toString())){
            minioService.clearBucket(id.toString());
            minioService.removeBucket(id.toString());
        }
        return CommonResult.ok();
    }

}

