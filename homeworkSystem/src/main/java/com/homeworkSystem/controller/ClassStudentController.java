package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homeworkSystem.entity.ClassStudent;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.entity.UserRole;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.service.ClassService;
import com.homeworkSystem.service.ClassStudentService;
import com.homeworkSystem.service.UserRoleService;
import com.homeworkSystem.service.UserService;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResultCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hs/class-student")
public class ClassStudentController {
    @Autowired
    ClassStudentService classStudentService;
    @Autowired
    UserRoleService userRoleService;

    @ApiOperation("批量添加学生到班级")
    @PostMapping("/addstudent")
    public CommonResult addStudent(@ApiParam("班级id") @RequestParam(value = "cid") Long cid,@ApiParam("学生ids") @RequestParam(value="studentsId") List<Long> studentsId){
        for(Long id :studentsId){
            //判断添加用户是否为学生
            UserRole ur=userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id",id));
            if(ur==null||!ur.getRoleId().equals(3L)){
                throw new MyException(ResultCode.NotStudent);
            }


            ClassStudent cs=classStudentService.getOne(new QueryWrapper<ClassStudent>().eq("class_id",cid).eq("student_id",id));
            if(cs==null){
                cs=new ClassStudent();
                cs.setClassId(cid);
                cs.setStudentId(id);
                classStudentService.save(cs);
            }
        }
        return CommonResult.ok();
    }

    @ApiOperation("批量将学生从班级删除")
    @DeleteMapping("/deletestudent")
    public CommonResult deleteStudent(@ApiParam("班级id") @RequestParam(value = "cid") Long cid,@ApiParam("学生ids") @RequestParam(value="studentsId") List<Long> studentsId){
        for(Long id :studentsId){
            LambdaQueryWrapper<ClassStudent> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(ClassStudent::getClassId,cid).eq(ClassStudent::getStudentId,id);
            classStudentService.remove(wrapper);
        }
        return CommonResult.ok();
    }
}

