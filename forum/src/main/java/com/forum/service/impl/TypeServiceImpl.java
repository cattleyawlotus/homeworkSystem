package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.entity.Type;
import com.forum.handler.exceptionhandler.MyException;
import com.forum.mapper.TypeMapper;
import com.forum.service.TypeService;
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
    public IPage<Type> queryByPage(int currPage, int pageSize) {
        IPage<Type> res=baseMapper.selectPage(new Page<>(currPage,pageSize),new LambdaQueryWrapper<>());
        return res;
    }
}
