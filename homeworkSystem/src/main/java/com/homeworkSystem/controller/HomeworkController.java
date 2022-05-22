package com.homeworkSystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homeworkSystem.entity.Homework;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.minio.service.MinioService;
import com.homeworkSystem.service.HomeworkService;
import com.homeworkSystem.service.HomeworkStudentService;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.ResultCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/hs/homework")
public class HomeworkController {
    
    @Autowired
    HomeworkService homeworkService;
    @Autowired
    HomeworkStudentService homeworkStudentService;
    @Autowired
    MinioService minioService;

    @ApiOperation("发布作业")
    @PostMapping("/createhw")
    public CommonResult createHw(@ApiParam("班级id")@RequestParam(value = "cid") Long cid, @ApiParam("作业标题")@RequestParam(value="name")String name, @RequestParam(value = "file")MultipartFile file){
        int number=homeworkService.count(new QueryWrapper<Homework>().eq("class_id",cid));
        Homework newHomework=new Homework();
        newHomework.setClassId(cid);
        newHomework.setNumber(number+1);
        newHomework.setName(name);
        newHomework.setFileName(file.getOriginalFilename());
        homeworkService.save(newHomework);
        //上传文件 返回url
        String url= minioService.putObject(cid.toString(),newHomework.getId().toString()+"/"+file.getOriginalFilename(),file);
        newHomework.setUrl(url);
        homeworkService.updateById(newHomework);

        return CommonResult.ok();
    }

    @ApiOperation("修改作业")
    @PutMapping("/updatehw")
    public CommonResult updateHw(@ApiParam("作业id") @RequestParam(value = "hid") Long hid, @ApiParam("作业标题")@RequestParam(value="name")String name, @RequestParam(value = "file")MultipartFile file){
        Homework hw=homeworkService.getOne(new QueryWrapper<Homework>().eq("id",hid));
        if(hw==null){
            throw new MyException(ResultCode.HomeworkNotExsist);
        }
        //删除上一次上传的文件
        String bucket=hw.getClassId().toString();
        String objectName=hid.toString()+"/"+hw.getUrl().substring(hw.getUrl().lastIndexOf("/")+1);
        minioService.removeObject(bucket,objectName);

        String url= minioService.putObject(hw.getClassId().toString(),hid.toString()+"/"+file.getOriginalFilename(),file);
        hw.setUrl(url);
        hw.setName(name);
        hw.setFileName(file.getOriginalFilename());
        homeworkService.updateById(hw);
        return CommonResult.ok();
    }

}
