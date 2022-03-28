package com.forum.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "分页请求封装类")
public class PageReq<T> {

    @ApiModelProperty("请求页码,默认置1")
    @Min(value = 1, message = "页码必须为正整数")
    private int currPage=1;

    @ApiModelProperty("页面容量默认置15")
    @Min(value = 1, message = "每页条数必须为正整数")
    private int pageSize=15;

//    @Valid
//    private T bean;
//
//    public int getOffset() {
//        return (getCurrPage() - 1) * getPageSize();
//    }
}