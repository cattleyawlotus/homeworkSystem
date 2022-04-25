package com.homeworkSystem.Vo.statistics;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class BlogView {

    @JsonSerialize(using = ToStringSerializer.class)
    Long id;

    String title;
    Integer thumbs;
    Integer views;
    Integer collects;
    Integer comments;
    Integer total;
}
