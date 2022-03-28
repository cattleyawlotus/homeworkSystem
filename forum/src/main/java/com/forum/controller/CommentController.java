package com.forum.controller;


import com.forum.Vo.BlogVo;
import com.forum.Vo.CommentVo;
import com.forum.Vo.PageReq;
import com.forum.entity.Blog;
import com.forum.entity.Comment;
import com.forum.service.CommentService;
import com.forum.utils.CommonResult;
import com.forum.utils.PageUtil;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@RestController
@RequestMapping("/forum/comment")
@Api(tags="Comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @ApiOperation("用户评论")
    @PostMapping("/comment")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
    public CommonResult comment(@RequestParam("content")String content,@RequestParam("bid")Long bid,@RequestParam(value = "pcid",required = false)Long pcid) {
        commentService.create(content,bid,pcid);
        return CommonResult.ok();
    }

    @ApiOperation("用户删除评论")
    @DeleteMapping("/deletecomment")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
    public CommonResult deleteComment(@RequestParam(value = "cid")Long cid) {
        commentService.deleteByUser(cid);
        return CommonResult.ok();
    }

    @ApiOperation("用户修改评论")
    @PutMapping("/updatecomment")
    @PreAuthorize("hasAnyRole('ROLE_admin','ROLE_user')")
    public CommonResult updateComment(@RequestParam(value = "cid")Long cid,@RequestParam("content")String content) {
        commentService.updateByUser(cid,content);
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

