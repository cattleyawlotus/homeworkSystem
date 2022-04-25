package com.homeworkSystem.service;

import com.homeworkSystem.Vo.LocationVo;
import com.homeworkSystem.entity.Views;
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
public interface ViewsService extends IService<Views> {

    void create(Long bid,String ip);

    void deleteByBlogId(Long bid);

    List<LocationVo> getLocation();

    List<Integer> getLastSevenDaysData();
}
