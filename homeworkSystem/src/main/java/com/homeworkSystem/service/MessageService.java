package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface MessageService extends IService<Message> {

    void create(String message, Long uid);
    List<MessageVo> getMessage();
    IPage<MessageVo> queryByPage(int currPage, int pageSize,String message);
    List<Integer> getLastSevenDaysData();

}
