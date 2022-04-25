package com.homeworkSystem.service;

import com.homeworkSystem.entity.Thumbs;
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
public interface ThumbsService extends IService<Thumbs> {

    Boolean create(Long uid, Long bid);

    Boolean query(Long uid, Long bid);

    void deleteByBlogId(Long bid);

    List<Integer> getLastSevenDaysData();

    void delete(Long uid, Long bid);
}
