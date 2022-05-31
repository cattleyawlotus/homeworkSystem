package com.homeworkSystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.homeworkSystem.entity.HomeworkStudent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzr
 * @since 2022-05-21
 */
public interface HomeworkStudentService extends IService<HomeworkStudent> {

    IPage<HomeworkStudent> getHomework(int currPage, int pageSize, Long hid);
}
