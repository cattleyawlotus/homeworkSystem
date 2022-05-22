package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.Vo.CommentVo;
import com.homeworkSystem.entity.Comment;
import com.homeworkSystem.entity.User;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.mapper.CommentMapper;
import com.homeworkSystem.service.
        CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.service.UserService;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    UserService userService;

    @Override
    public IPage<Comment> getComment(Long id,int curr, int size) {
        IPage<Comment>page=new Page<>(curr,size);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getBlogId, id)
                .eq(Comment::getParentCommentId, -1)
                .eq(Comment::getTopComment,-1)
                .orderByDesc(Comment::getCreateTime);
        return baseMapper.selectPage(page,wrapper);
    }

    @Override
    public void create(Long uid,String content, Long bid, Long pcid) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBlogId(bid);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,uid);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new MyException(ResultCode.UserNotExist);
        }
        comment.setUid(user.getId());
        comment.setUser(user.getUsername());
        comment.setAvatar(user.getAvatar());
        if (pcid != null) {
            LambdaQueryWrapper<Comment> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(Comment::getId, pcid);
            Comment father = baseMapper.selectOne(wrapper2);
            if (father == null || !father.getBlogId().equals(bid)) {
                throw new MyException(ResultCode.FatherCommentError);
            } else {
                if(father.getTopComment()==-1&&father.getParentCommentId()==-1){
                    comment.setParentCommentId(pcid);
                    comment.setTopComment(pcid);
                }else if(father.getTopComment()!=-1&&father.getParentCommentId()!=-1){
                    comment.setParentCommentId(pcid);
                    comment.setTopComment(father.getTopComment());
                }else{
                    throw new MyException(ResultCode.FatherCommentError);
                }
            }
        } else {
            comment.setParentCommentId(-1L);
            comment.setTopComment(-1L);
        }
        baseMapper.insert(comment);
    }

    @Override
    public List<CommentVo> setReply(Comment top) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTopComment, top.getId())
                .orderByAsc(Comment::getCreateTime);
        List<Comment> commentList = baseMapper.selectList(wrapper);
        if (commentList.size() == 0) {
            return new ArrayList<>();
        } else {
            return commentList.stream().map(x -> {
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(x, commentVo);
                commentVo.setTime(x.getCreateTime());
                Comment parent=baseMapper.selectOne(new LambdaQueryWrapper<Comment>().eq(Comment::getId,x.getParentCommentId()));
                if(parent!=null){
                    commentVo.setParentName(parent.getUser());
                }
                //点赞数 待修改
                commentVo.setThumbs(10);
                return commentVo;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void deleteByBlogId(Long bid) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getBlogId, bid);
        baseMapper.delete(wrapper);
    }

    @Override
    public void deleteByUser(Long cid,Long uid) {
        LambdaQueryWrapper<Comment>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Comment::getId,cid).eq(Comment::getUid, uid);
        Comment comment=baseMapper.selectOne(wrapper1);
        if(comment!=null){
            LambdaQueryWrapper<Comment>wrapper2=new LambdaQueryWrapper<>();
            if(comment.getTopComment()==-1&&comment.getParentCommentId()==-1){
                wrapper2.eq(Comment::getTopComment,cid);
            }else{
                wrapper2.eq(Comment::getParentCommentId,cid);
            }
            List<Comment>commentList= baseMapper.selectList(wrapper2);
            commentList.forEach(x->{
                deleteByManager(x.getId());
            });
        }else{
            throw new MyException(ResultCode.CommentNotExsist);
        }
        baseMapper.delete(wrapper1);
    }

    @Override
    public void updateByUser(Long uid,Long cid, String content) {
        LambdaQueryWrapper<Comment>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Comment::getId,cid).eq(Comment::getUid,uid);
        Comment comment=baseMapper.selectOne(wrapper1);
        if(comment!=null){
            comment.setContent(content);
            baseMapper.updateById(comment);
        }else{
            throw new MyException(ResultCode.CommentNotExsist);
        }
    }

    @Override
    public IPage<Comment> getCommentByManager(int curr, int size,String comment) {
        IPage<Comment>page=new Page<>(curr,size);
        LambdaQueryWrapper<Comment>wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Comment::getUpdateTime);
        if(comment!=null){
            wrapper.like(Comment::getContent,comment);
        }
        return baseMapper.selectPage(page,wrapper);
    }

    @Override
    public void deleteByManager(Long cid) {
        LambdaQueryWrapper<Comment>wrapper1=new LambdaQueryWrapper<>();
        wrapper1.eq(Comment::getId,cid);
        Comment comment=baseMapper.selectOne(wrapper1);
        if(comment!=null){
            LambdaQueryWrapper<Comment>wrapper2=new LambdaQueryWrapper<>();
            if(comment.getTopComment()==-1&&comment.getParentCommentId()==-1){
                wrapper2.eq(Comment::getTopComment,cid);
            }else{
                wrapper2.eq(Comment::getParentCommentId,cid);
            }
            List<Comment>commentList= baseMapper.selectList(wrapper2);
            commentList.forEach(x->{
                deleteByManager(x.getId());
            });
        }else{
            throw new MyException(ResultCode.CommentNotExsist);
        }
        baseMapper.delete(wrapper1);
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Comment>().between(Comment::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }
}
