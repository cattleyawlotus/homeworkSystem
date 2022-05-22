package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.Vo.statistics.BlogData;
import com.homeworkSystem.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface BlogService extends IService<Blog> {

    IPage<Blog> getAllBlog(int curr,int size,String title);
    IPage<Blog> getUserBlog(int currPage, int pageSize, long uid);
    IPage<Blog> getBlogByType(String type,int curr,int size);
    IPage<Blog> getBlogByTypeId(Long id, int curr, int size);
    IPage<Blog> getBlogByTag(String tag, int curr, int size);
    IPage<Blog> getBlogByTagId(Long id, int cur, int size);
    IPage<Blog> getCollectedBlog(int currPage, int pageSize,Long uid);

    List<Blog> getLatestBlog(long hours);
    Blog getBlogById(Long id);

    void updateThumbs(Long bid);
    void updateViews(Long bid);
    void updateCollects(Long bid);


    boolean delete(Long bid, Long uid);


    List<Integer> getLastSevenDaysData();
    List<BlogData> getBlogDataByType();

}
