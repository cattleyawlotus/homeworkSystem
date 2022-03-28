package com.forum.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.forum.Vo.MessageVo;
import com.forum.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageVo> getMessage();

    IPage<MessageVo> queryByPage(IPage<Message> page);
}
