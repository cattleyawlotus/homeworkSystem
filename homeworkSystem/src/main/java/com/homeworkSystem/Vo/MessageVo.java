package com.homeworkSystem.Vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class MessageVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String avatar;
    private String nickname;
    private String messageContent;
    private double time;

}
