package com.homeworkSystem.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.Vo.LocationVo;
import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.Vo.PageReq;
import com.homeworkSystem.Vo.RegisterVo;
import com.homeworkSystem.Vo.statistics.BlogData;
import com.homeworkSystem.Vo.statistics.BlogView;
import com.homeworkSystem.entity.*;
import com.homeworkSystem.service.*;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hs/manager")
@Api(tags="Manager")
public class ManagerController {

    @Autowired
    UserService userService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;
    @Autowired
    ViewsService viewsService;
    @Autowired
    ThumbsService thumbsService;
    @Autowired
    CollectsService collectsService;
    @Autowired
    BlogService blogService;
    @Autowired
    MessageService messageService;
    @Autowired
    BlogViewService blogViewService;

    //<editor-fold desc="用户">
    @ApiOperation("添加用户")
    @PostMapping("/adduser")
    public CommonResult addUser(@RequestBody RegisterVo user) {
        userService.register(user);
        return CommonResult.ok();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/deleteuser")
    public CommonResult deleteUser(@RequestParam(value = "id")Long id) {
        userService.delete(id);
        return CommonResult.ok();
    }

    @ApiOperation("分页查询用户")
    @GetMapping("/queryuser")
    public CommonResult queryUser(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String userName) {
        PageUtil<User> page = new PageUtil<>(userService.queryByPage(req.getCurrPage(),req.getPageSize(),userName));
        return CommonResult.ok().data(page);
    }

    @ApiOperation("根据用户名查询用户")
    @GetMapping("/querybyname")
    public CommonResult querybyname(@RequestParam(value = "username") String username){
        User user=userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,username));
        return CommonResult.ok().data(user);
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("/querybyid")
    public CommonResult query(@RequestParam(value = "id") Long id){
        User user=userService.getById(id);
        return CommonResult.ok().data(user);
    }
    //</editor-fold>

    //<editor-fold desc="type">
    @ApiOperation("添加类型")
    @PostMapping("/addtype")
    public CommonResult addType(@RequestParam(value = "typeName")String typeName) {
        typeService.addType(typeName);
        return CommonResult.ok();
    }

    @ApiOperation("删除类型")
    @DeleteMapping("/deletetype")
    public CommonResult deleteType(@RequestParam(value = "id")Long id) {
        typeService.deleteType(id);
        return CommonResult.ok();
    }

    @ApiOperation("更新类型名称")
    @PutMapping("/updatetype")
    public CommonResult updateType(@RequestParam(value = "id")Long id,@RequestParam(value = "typeName")String typeName) {
        typeService.updateType(id,typeName);
        return CommonResult.ok();
    }

    @ApiOperation("分页查询类型")
    @GetMapping("/querytype")
    public CommonResult queryType(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String typeName) {
        PageUtil<Type> page = new PageUtil<>(typeService.queryByPage(req.getCurrPage(),req.getPageSize(),typeName));
        return CommonResult.ok().data(page);
    }
    //</editor-fold>

    //<editor-fold desc="tag">
    @ApiOperation("添加标签")
    @PostMapping("/addtag")
    public CommonResult addTag(@RequestParam(value = "tagName")String tagName) {
        tagService.addTag(tagName);
        return CommonResult.ok();
    }

    @ApiOperation("删除标签")
    @DeleteMapping("/deletetag")
    public CommonResult deleteTag(@RequestParam(value = "id")Long id) {
        tagService.deleteTag(id);
        return CommonResult.ok();
    }

    @ApiOperation("更新标签名称")
    @PutMapping("/updatetag")
    public CommonResult updateTag(@RequestParam(value = "id")Long id,@RequestParam(value = "tagName")String tagName) {
        tagService.updateTag(id,tagName);
        return CommonResult.ok();
    }

    @ApiOperation("分页查询标签")
    @GetMapping("/querytag")
    public CommonResult queryTag(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String tagName) {
        PageUtil<Tag> page = new PageUtil<>(tagService.queryByPage(req.getCurrPage(),req.getPageSize(),tagName));
        return CommonResult.ok().data(page);
    }
    //</editor-fold>

    //<editor-fold desc="评论">
    @ApiOperation("按时间分页查询所有评论")
    @GetMapping("/getcomment")
    public CommonResult getComment(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String comment) {
        PageUtil<Comment> page = new PageUtil<>(commentService.getCommentByManager(req.getCurrPage(),req.getPageSize(),comment));
        return CommonResult.ok().data(page);
    }

    @ApiOperation("管理员删除评论")
    @DeleteMapping("/deletecomment")
    public CommonResult deleteComment(@RequestParam(value = "cid")Long cid) {
         commentService.deleteByManager(cid);
        return CommonResult.ok();
    }

    //</editor-fold>

    //<editor-fold desc="留言">
    @ApiOperation("分页查询留言")
    @GetMapping("/meaasge")
    public CommonResult queryMeaasge(@Validated @ApiParam("分页参数") PageReq req,@RequestParam(required = false)String message) {
        PageUtil<MessageVo> page = new PageUtil<>(messageService.queryByPage(req.getCurrPage(),req.getPageSize(),message));
        return CommonResult.ok().data(page);
    }

    @ApiOperation("删除留言")
    @DeleteMapping("/deletemeaasge")
    public CommonResult deleteMeaasge(@RequestParam(value = "id")Long id) {
         messageService.removeById(id);
        return CommonResult.ok();
    }
    //</editor-fold>

    //<editor-fold desc="位置">
    @ApiOperation("查询浏览记录的位置信息")
    @GetMapping("/location")
    public CommonResult location() {
        List<LocationVo>list=viewsService.getLocation();
        return CommonResult.ok().data(list);
    }
    //</editor-fold>

    //<editor-fold desc="数据统计">
    @ApiOperation("查询数据总览")
    @GetMapping("/data")
    public CommonResult getData() {
        Map<String,Long> data=new HashMap<>();
        data.put("views", (long) viewsService.count());
        data.put("users", (long) userService.count());
        data.put("blogs", (long) blogService.count());
        data.put("message", (long) messageService.count());
        return CommonResult.ok().data(data);
    }
    @ApiOperation("查询过去七天每日总体增长数据")
    @GetMapping("/last7daysdata")
    public CommonResult getLastSevenDaysData(@RequestParam(value = "type")String type) {
        List<List<Integer>>data=new ArrayList<>();
        List<Integer>actual=new ArrayList<>();
        List<Integer>actualfake=new ArrayList<>();
        List<Integer>expect=new ArrayList<>();
        if(type.equals("views")){
            actual=viewsService.getLastSevenDaysData();
        }else if(type.equals("messages")){
            actual=messageService.getLastSevenDaysData();
        }else if(type.equals("blogs")){
            actual=blogService.getLastSevenDaysData();
        }else if(type.equals("users")){
            actual=userService.getLastSevenDaysData();
        }else if(type.equals("thumbs")){
            actual=thumbsService.getLastSevenDaysData();
        }
        //伪造数据
        actual.forEach(x->{
            Integer y=(int)Math.round((x+50)*(1+Math.random()*2-1));
            expect.add((int)Math.round(y*(1+Math.random()*2-1)));
            actualfake.add(y);
        });
        data.add(actualfake);
        data.add(expect);
        return CommonResult.ok().data(data);
    }

    @ApiOperation("博客统计数据（按type）")
    @GetMapping("/blogdata")
    public CommonResult getblogdata() {
        List<BlogData>data=blogService.getBlogDataByType();
        return CommonResult.ok().data(data);
    }
    @ApiOperation("查询过去七天博客总体增长数据(点赞、评论、收藏)")
    @GetMapping("/last7daysblogdata")
    public CommonResult getLastSevenDaysBlogData() {
        Map<String,List<Integer>>data=new HashMap<>();
        List<Integer>thumbs=thumbsService.getLastSevenDaysData().stream().map(x-> (int)Math.round((x+30)*(1+Math.random()*2-1))).collect(Collectors.toList());
        List<Integer>comments=commentService.getLastSevenDaysData().stream().map(x-> (int)Math.round((x+30)*(1+Math.random()*2-1))).collect(Collectors.toList());
        List<Integer>collects=collectsService.getLastSevenDaysData().stream().map(x-> (int)Math.round((x+30)*(1+Math.random()*2-1))).collect(Collectors.toList());
        data.put("thumbs",thumbs);
        data.put("comments",comments);
        data.put("collects",collects);
        return CommonResult.ok().data(data);
    }

    @ApiOperation("查询博客排名数据(点赞、评论、收藏)")
    @GetMapping("/blogrank")
    public CommonResult blogRank() {
        List<BlogView>res=blogViewService.list();
        return CommonResult.ok().data(res);
    }


    //</editor-fold>


}
