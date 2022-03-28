package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.entity.Tag;
import com.forum.handler.exceptionhandler.MyException;
import com.forum.mapper.TagMapper;
import com.forum.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.forum.utils.ResultCode;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public void addTag(String tagName) {
        if(baseMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName,tagName))==null){
            Tag tag=new Tag();
            tag.setTagName(tagName);
            baseMapper.insert(tag);
        }else{
            throw new MyException(ResultCode.TagExsist);
        }
    }

    @Override
    public void deleteTag(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<Tag>().eq(Tag::getId,id));
    }

    @Override
    public IPage<Tag> queryByPage(int currPage, int pageSize) {
        IPage<Tag> res=baseMapper.selectPage(new Page<>(currPage,pageSize),new LambdaQueryWrapper<>());
        return res;
    }
}
