package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.Vo.RegisterVo;
import com.homeworkSystem.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    User selectByUsername(String username);

    String login(User user);

    void register(RegisterVo user);

    void delete(Long id);

    IPage<User> queryByPage(int currPage, int pageSize,String userName);

    List<Integer> getLastSevenDaysData();

    void sendBlogMessage(Long bid, String message);
    int sendOffLineMessage(String uid);
}
