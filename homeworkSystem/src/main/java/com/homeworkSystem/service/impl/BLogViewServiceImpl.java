package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.Vo.statistics.BlogView;
import com.homeworkSystem.mapper.BlogViewMapper;
import com.homeworkSystem.service.BlogViewService;
import org.springframework.stereotype.Service;


@Service
public class BLogViewServiceImpl extends ServiceImpl<BlogViewMapper, BlogView> implements BlogViewService {

}