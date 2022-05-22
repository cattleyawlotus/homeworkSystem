package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;


public interface TypeService extends IService<Type> {

    void addType(String typeName);

    void deleteType(Long id);

    void updateType(Long id, String typeName);

    IPage<Type> queryByPage(int currPage, int pageSize,String typeName);

}
