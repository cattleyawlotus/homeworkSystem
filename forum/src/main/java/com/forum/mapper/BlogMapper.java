package com.forum.mapper;

import com.forum.Vo.statistics.BlogData;
import com.forum.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
public interface BlogMapper extends BaseMapper<Blog> {

    List<BlogData> getblogdata();
}
