package com.homeworkSystem.service;

import com.homeworkSystem.entity.Thumbs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ThumbsService extends IService<Thumbs> {

    Boolean create(Long uid, Long bid);

    Boolean query(Long uid, Long bid);

    void deleteByBlogId(Long bid);

    List<Integer> getLastSevenDaysData();

    void delete(Long uid, Long bid);
}
