package com.homeworkSystem.Vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.homeworkSystem.entity.Tag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogVo {

    @TableId(value = "id")
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
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    private String username;

    private String avatar;

    private String type;

    private List<Tag> tags;
}
