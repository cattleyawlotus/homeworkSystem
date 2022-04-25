package com.homeworkSystem.mapper;

import com.homeworkSystem.Vo.statistics.BlogData;
import com.homeworkSystem.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface BlogMapper extends BaseMapper<Blog> {

    List<BlogData> getblogdata();
}
