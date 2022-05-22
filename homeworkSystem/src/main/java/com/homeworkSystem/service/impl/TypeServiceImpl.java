package com.homeworkSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homeworkSystem.entity.Type;
import com.homeworkSystem.handler.exceptionhandler.MyException;
import com.homeworkSystem.mapper.TypeMapper;
import com.homeworkSystem.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homeworkSystem.utils.ResultCode;
import org.springframework.stereotype.Service;


@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    @Override
    public void addType(String typeName) {
        if(baseMapper.selectOne(new LambdaQueryWrapper<Type>().eq(Type::getTypeName,typeName))==null){
            Type type=new Type();
            type.setTypeName(typeName);
            baseMapper.insert(type);
        }else{
            throw new MyException(ResultCode.TypeExsist);
        }
    }

    @Override
    public void deleteType(Long id) {
        baseMapper.delete(new LambdaQueryWrapper<Type>().eq(Type::getId,id));
    }

    @Override
    public void updateType(Long id, String typeName) {
        Type type=baseMapper.selectById(id);
        if(type!=null){
            type.setTypeName(typeName);
            baseMapper.updateById(type);
        }else{
            throw new MyException(ResultCode.TypeNotExsist);
        }
    }

    @Override
    public IPage<Type> queryByPage(int currPage, int pageSize,String typeName) {
        LambdaQueryWrapper<Type> wrapper=new LambdaQueryWrapper();
        if(typeName!=null){
            wrapper.like(Type::getTypeName,typeName);
        }
        IPage<Type> res=baseMapper.selectPage(new Page<>(currPage,pageSize),wrapper);
        return res;
    }
}
