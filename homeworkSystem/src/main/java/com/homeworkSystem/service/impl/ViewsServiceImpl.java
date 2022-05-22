package com.homeworkSystem.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.homeworkSystem.Vo.LocationVo;
import com.homeworkSystem.entity.IpLocation;
import com.homeworkSystem.entity.Views;
import com.homeworkSystem.mapper.ViewsMapper;
import com.homeworkSystem.service.BlogService;
import com.homeworkSystem.service.IpLocationService;
import com.homeworkSystem.service.ViewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ViewsServiceImpl extends ServiceImpl<ViewsMapper, Views> implements ViewsService {

    @Autowired
    BlogService blogService;
    @Autowired
    IpLocationService ipLocationService;
    @Override
    public void create(Long bid,String ip) {
        Views views=new Views();
        views.setBlogId(bid);
        views.setIp(ip);
        baseMapper.insert(views);

        LambdaQueryWrapper<IpLocation>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(IpLocation::getIp,ip);
        IpLocation ipLocation=ipLocationService.getOne(wrapper);
        if(ipLocation==null) {
            String content = HttpUtil.get("https://api.map.baidu.com/location/ip?ak=jYXtxApUfx8t4FkFxxXNSK6DHi8G8DXu&coor=bd09ll&ip=" + ip);
            JSONObject json = new JSONObject(content);
            JSONObject dispositionNotificationListObject = json.getJSONObject("content");
            Map point = (Map) dispositionNotificationListObject.get("point");
            Map addressdetail =(Map)dispositionNotificationListObject.get("address_detail");
            ipLocation = new IpLocation();
            ipLocation.setIp(ip);
            ipLocation.setX(point.get("x").toString());
            ipLocation.setY(point.get("y").toString());
            ipLocation.setCity(addressdetail.get("city").toString());
            ipLocationService.save(ipLocation);
        }
    }

    @Override
    public void deleteByBlogId(Long bid) {
        LambdaQueryWrapper<Views>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Views::getBlogId,bid);
        baseMapper.delete(wrapper);
    }

    @Override
    public List<LocationVo> getLocation() {
        LocalDateTime left=LocalDateTime.now().minusDays(10000);
        List<Views>list=baseMapper.selectList(new LambdaQueryWrapper<Views>().between(Views::getCreateTime,left,LocalDateTime.now()));

        List<LocationVo>res=new ArrayList<>();
        Map<String,String[]>ips=new HashMap<>();
        list.forEach(x->{
            String []arr=new String[2];
            if(ips.containsKey(x.getIp())){
                arr[0]=ips.get(x.getIp())[0];
                arr[1]=ips.get(x.getIp())[0];
            }else{
                LambdaQueryWrapper<IpLocation>wrapper=new LambdaQueryWrapper<>();
                wrapper.eq(IpLocation::getIp,x.getIp());
                IpLocation ipLocation=ipLocationService.getOne(wrapper);
                arr[0]=ipLocation.getX();
                arr[1]=ipLocation.getY();
                ips.put(x.getIp(),arr);
            }
            LocationVo locationVo=new LocationVo();
            arr[0]=String.valueOf(Double.valueOf(arr[0])+((Math.random()*4)-2));
            arr[1]=String.valueOf(Double.valueOf(arr[1])+((Math.random()*4)-2));
            locationVo.setValue(arr);
            res.add(locationVo);
        });

        return  res;
    }

    @Override
    public List<Integer> getLastSevenDaysData() {
        List<Integer>res=new ArrayList<>();
        for(int i=0;i<7;i++){
            LocalDateTime left=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i+1);
            LocalDateTime right=LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(i);
            Integer num=baseMapper.selectCount(new LambdaQueryWrapper<Views>().between(Views::getCreateTime,left,right));
            res.add(num);
        }
        return res;
    }

}
