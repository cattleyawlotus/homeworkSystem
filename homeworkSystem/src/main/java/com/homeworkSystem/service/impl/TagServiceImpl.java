package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.entity.Tag;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.mapper.TagMapper;
import com.homeworkSystem.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.stereotype.Service;


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
    public void updateTag(Long id, String tagName) {
        Tag tag=baseMapper.selectById(id);
        if(tag!=null){
            tag.setTagName(tagName);
            baseMapper.updateById(tag);
        }else{
            throw new MyException(ResultCode.TagNotExsist);
        }
    }

    @Override
    public IPage<Tag> queryByPage(int currPage, int pageSize,String tagName) {
        LambdaQueryWrapper<Tag>wrapper=new LambdaQueryWrapper<>();
        if(tagName!=null){
            wrapper.like(Tag::getTagName,tagName);
        }
        IPage<Tag> res=baseMapper.selectPage(new Page<>(currPage,pageSize),wrapper);
        return res;
    }
}
