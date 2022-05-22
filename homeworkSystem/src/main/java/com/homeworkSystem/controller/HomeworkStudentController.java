package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.homeworkSystem.entity.ClassStudent;
import com.homeworkSystem.entity.Homework;
import com.homeworkSystem.entity.HomeworkStudent;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.minio.service.MinioService;
import com.homeworkSystem.service.ClassStudentService;
import com.homeworkSystem.service.HomeworkService;
import com.homeworkSystem.service.HomeworkStudentService;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.FileUtil;
import com.homeworkSystem.utils.ResultCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-05-21
 */
@RestController
@RequestMapping("/hs/homework-student")
public class HomeworkStudentController {
    @Autowired
    HomeworkService homeworkService;
    @Autowired
    HomeworkStudentService homeworkStudentService;
    @Autowired
    ClassStudentService classStudentService;
    @Autowired
    MinioService minioService;

    @ApiOperation("提交作业")
    @PostMapping("/uploadhw")
    public CommonResult uploadHw(@ApiParam("作业id") @RequestParam(value = "hid") Long hid,@ApiParam("学生id") @RequestParam(value = "sid") Long sid, @RequestParam(value = "file") MultipartFile file){
        Homework hw=homeworkService.getOne(new QueryWrapper<Homework>().eq("id",hid));
        if(hw==null){
            throw new MyException(ResultCode.HomeworkNotExsist);
        }
        ClassStudent cs=classStudentService.getOne(new QueryWrapper<ClassStudent>().eq("class_id",hw.getClassId()).eq("student_id",sid));
        if(cs==null){
            throw new MyException(ResultCode.StudentClassNotMatch);
        }

        LambdaQueryWrapper<HomeworkStudent>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(HomeworkStudent::getHomeworkId,hid).eq(HomeworkStudent::getStudentId,sid);
        HomeworkStudent hs=homeworkStudentService.getOne(wrapper);
        if(hs==null){
            hs=new HomeworkStudent();
            hs.setHomeworkId(hid);
            hs.setStudentId(sid);
            //上传文件
            String url= minioService.putObject(hw.getClassId().toString(),hid.toString()+"/"+sid.toString()+"/"+file.getOriginalFilename(),file);
            hs.setUrl(url);
            hs.setFileName(file.getOriginalFilename());
            homeworkStudentService.save(hs);
        }else{
            //删除上一次上传的文件
            String bucket=hw.getClassId().toString();
            String objectName=hid.toString()+"/"+sid.toString()+"/"+hs.getUrl().substring(hs.getUrl().lastIndexOf("/")+1);
            minioService.removeObject(bucket,objectName);
            //重新上传文件
            String url= minioService.putObject(hw.getClassId().toString(),hid.toString()+"/"+sid.toString()+"/"+file.getOriginalFilename(),file);
            hs.setUrl(url);
            hs.setFileName(file.getOriginalFilename());
            homeworkStudentService.updateById(hs);
        }
        return CommonResult.ok();
    }

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;

    @ApiOperation("批量下载作业")
    @GetMapping("/downloadhw")
    public CommonResult uploadHw(@ApiParam("作业id") @RequestParam(value = "hid") Long hid){
        List<HomeworkStudent> homeworkStudentList=homeworkStudentService.list(new QueryWrapper<HomeworkStudent>().eq("homework_id",hid));


        List<Map<String, String>> mapList = new ArrayList<>();
        for(HomeworkStudent hs:homeworkStudentList){
            Map<String, String> map = new HashMap<>();
            map.put("path", hs.getUrl());
            map.put("name", hs.getFileName());
            mapList.add(map);
        }

        FileUtil.zipDirFileToFile(mapList, request, response);
        return CommonResult.ok();
    }
}

