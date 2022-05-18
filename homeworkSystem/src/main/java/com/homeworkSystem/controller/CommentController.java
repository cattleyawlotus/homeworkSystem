package com.homeworkSystem.controller;


import com.homeworkSystem.Vo.CommentVo;
import com.homeworkSystem.Vo.PageReq;
import com.homeworkSystem.entity.Comment;
import com.homeworkSystem.service.CommentService;
import com.homeworkSystem.service.UserService;
import com.homeworkSystem.utils.CommonResult;
import com.homeworkSystem.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/hs/comment")
@Api(tags="Comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @ApiOperation("用户评论")
    @PostMapping("/comment")
    public CommonResult comment(@RequestParam(value = "uid")Long uid,@RequestParam("content")String content,@RequestParam("bid")Long bid,@RequestParam(value = "pcid",required = false)Long pcid) {
        commentService.create(uid,content,bid,pcid);
        userService.sendBlogMessage(bid,"您的帖子收到一个评论！");
        return CommonResult.ok();
    }

    @ApiOperation("用户删除评论")
    @DeleteMapping("/deletecomment")
    public CommonResult deleteComment(@RequestParam(value = "cid")Long cid,@RequestParam(value = "uid")Long uid) {
        commentService.deleteByUser(cid,uid);
        return CommonResult.ok();
    }

    @ApiOperation("用户修改评论")
    @PutMapping("/updatecomment")
    public CommonResult updateComment(@RequestParam(value = "uid")Long uid,@RequestParam(value = "cid")Long cid,@RequestParam("content")String content) {
        commentService.updateByUser(uid,cid,content);
        return CommonResult.ok();
    }

    @ApiOperation("分页展示博客的评论")
    @GetMapping("/getcomment")
    public CommonResult getComment(@RequestParam("id")Long id,@Validated @ApiParam("分页参数") PageReq req) {
        PageUtil<Comment>page = new PageUtil<>(commentService.getComment(id,req.getCurrPage(),req.getPageSize()));
        List<CommentVo> commentList=page.getList().stream().map(x->{
            CommentVo commentVo=new CommentVo();
            BeanUtils.copyProperties(x,commentVo);
            commentVo.setReply(commentService.setReply(x));
            commentVo.setCommentNum(commentVo.getReply().size());
            commentVo.setTime(x.getCreateTime());
            //点赞数 待修改
            commentVo.setThumbs((int)Math.round(Math.random()*100));
            return commentVo;
        }).collect(Collectors.toList());
        PageUtil<CommentVo> res=new PageUtil<>(commentList,page.getTotalCount(),page.getPageSize(),page.getCurrPage());
        return CommonResult.ok().data(res);
    }

}

