package com.forum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.forum.Vo.CommentVo;
import com.forum.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface CommentService extends IService<Comment> {
    IPage<Comment> getComment(Long id,int curr, int size);

    void create(String content, Long bid, Long pcid);

    List<CommentVo> setReply(Comment top);

    void deleteByBlogId(Long bid);

    void deleteByUser(Long cid);

    void updateByUser(Long cid, String content);

    IPage<Comment> getCommentByManager(int curr, int size);

    void deleteByManager(Long cid);

    List<Integer> getLastSevenDaysData();
}
