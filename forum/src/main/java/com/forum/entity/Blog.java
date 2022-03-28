package com.forum.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzr
 * @since 2022-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Blog对象")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    @ApiModelProperty(value = "类型id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long typeId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "简介、摘要")
    private String description;

    @ApiModelProperty(value = "首图")
    private String firstPicture;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private String thumbs;

    @ApiModelProperty(value = "浏览量")
    private String views;

    @ApiModelProperty(value = "收藏量")
    private String collects;

    @ApiModelProperty(value = "版权状态：原创、转载、翻译")
    private String shareStatement;

    @ApiModelProperty(value = "是否发布 0:草稿 1:已发布")
    private boolean published;

    @ApiModelProperty(value = "是否开启推荐")
    private boolean recommend;

    @ApiModelProperty(value = "是否开启赞赏")
    private boolean appreciation;

    @ApiModelProperty(value = "是否开启评论")
    private boolean commentabled;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
