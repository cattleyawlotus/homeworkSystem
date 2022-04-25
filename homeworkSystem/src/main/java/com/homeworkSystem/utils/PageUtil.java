package com.homeworkSystem.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("分页")
public class PageUtil<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private int totalCount;
    /**
     * 每页记录数
     */
    @ApiModelProperty("页面容量默认置15")
    private int pageSize;
    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private int totalPage;
    /**
     * 当前页数
     */
    @ApiModelProperty("当前页码,默认置1")
    private int currPage;
    /**
     * 列表数据
     */
    @ApiModelProperty(value = "列表数据",name = "list")
    private List<T> list;

    /**
     * 分页
     * @param list        列表数据
     * @param totalCount  总记录数
     * @param pageSize    每页记录数
     * @param currPage    当前页数
     */
    public PageUtil(List<T> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * 分页
     */
    public PageUtil(IPage<T> page) {
        this.list = page.getRecords();
        this.totalCount = (int) page.getTotal();
        this.pageSize = (int) page.getSize();
        this.currPage = (int) page.getCurrent();
        this.totalPage = (int) page.getPages();
    }

}
