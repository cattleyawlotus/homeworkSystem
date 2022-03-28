package com.forum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.forum.entity.Collects;

import java.util.Collection;
import java.util.List;


public interface CollectsService extends IService<Collects> {

    Boolean create(Long uid, Long bid);

    Boolean query(Long uid, Long bid);

    void deleteByBlogId(Long bid);

    void delete(Long uid, Long bid);

    List<Integer> getLastSevenDaysData();
}