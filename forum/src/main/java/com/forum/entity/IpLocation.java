package com.forum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ip地址对应的坐标", description="")
public class IpLocation implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId(value = "ip")
    String Ip;
    String x;
    String y;
    String city;
}
