package com.forum.service;

import com.forum.Vo.LocationVo;
import com.forum.entity.Views;
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
