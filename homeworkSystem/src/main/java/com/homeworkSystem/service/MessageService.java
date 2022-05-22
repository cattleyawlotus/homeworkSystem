package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.Vo.MessageVo;
import com.homeworkSystem.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface MessageService extends IService<Message> {

    void create(String message, Long uid);
    List<MessageVo> getMessage();
    IPage<MessageVo> queryByPage(int currPage, int pageSize,String message);
    List<Integer> getLastSevenDaysData();

}
