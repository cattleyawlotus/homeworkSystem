package com.homeworkSystem.Vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long id;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long uid;

        private String user;

        private String avatar;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long blogId;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long topComment;

        @JsonSerialize(using = ToStringSerializer.class)
        private Long parentCommentId;

        private String parentName;

        private String content;

        private List<CommentVo> reply;

        private Date time;

        private Integer thumbs;

        private Integer commentNum;

        private Boolean inputShow=false;
}
