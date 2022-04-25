package com.homeworkSystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

    IPage<MessageVo> queryByPage(IPage<Message> page,  @Param(Constants.WRAPPER)LambdaQueryWrapper<Message>wrapper);
}
