package com.forum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.forum.Vo.RegisterVo;
import com.forum.entity.User;
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
public interface UserService extends IService<User> {
    User selectByUsername(String username);

    String login(User user);

    void register(RegisterVo user);

    void delete(Long id);

    IPage<User> queryByPage(int currPage, int pageSize);

    List<Integer> getLastSevenDaysData();
}
