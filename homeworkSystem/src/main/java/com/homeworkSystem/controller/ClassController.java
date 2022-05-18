package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.Vo.BlogVo;
import com.homeworkSystem.entity.Blog;
import com.homeworkSystem.entity.Class;
import com.homeworkSystem.entity.ClassStudent;
import com.homeworkSystem.service.ClassService;
import com.homeworkSystem.service.ClassStudentService;
import com.homeworkSystem.utils.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-04-25
 */
@RestController
@RequestMapping("/hs/class")
public class ClassController {
    @Autowired
    ClassService classService;
    @Autowired
    ClassStudentService classStudentService;

    @ApiOperation("新建班级")
    @PostMapping("/createblog")
    public CommonResult createClass(@RequestParam(value = "tid") Long tid,@RequestParam(value="className")String className){
        Class class1=new Class();
        class1.setName(className);
        class1.setTeacherId(tid);
        classService.save(class1);
        return CommonResult.ok();
    }

    @ApiOperation("批量添加学生到班级")
    @PostMapping("/addstudent")
    public CommonResult addStudent(@RequestParam(value = "cid") Long cid,@RequestParam(value="studentsId") List<Long> studentsId){
        for(Long id :studentsId){
            ClassStudent cs=new ClassStudent();
             cs.setClassId(cid);
             cs.setStudentId(id);
             classStudentService.save(cs);
        }
        return CommonResult.ok();
    }

    @ApiOperation("批量将学生从班级删除")
    @DeleteMapping("/deletestudent")
    public CommonResult deleteStudent(@RequestParam(value = "cid") Long cid,@RequestParam(value="studentsId") List<Long> studentsId){
        for(Long id :studentsId){
            LambdaQueryWrapper<ClassStudent>wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(ClassStudent::getClassId,cid).eq(ClassStudent::getStudentId,id);
            classStudentService.remove(wrapper);
        }
        return CommonResult.ok();
    }
}

