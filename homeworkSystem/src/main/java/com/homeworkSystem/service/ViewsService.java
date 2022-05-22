package com.homeworkSystem.service;

import com.homeworkSystem.Vo.LocationVo;
import com.homeworkSystem.entity.Views;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ViewsService extends IService<Views> {

    void create(Long bid,String ip);

    void deleteByBlogId(Long bid);

    List<LocationVo> getLocation();

    List<Integer> getLastSevenDaysData();
}
