package com.forum.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CommonResult<T> {

    private Boolean success;

    private String code;

    private String message;

//    private Map<String, Object> data = new HashMap<String, Object>();
    private T data;

    //把构造方法私有
    private CommonResult() {}

    //成功静态方法
    public static CommonResult ok() {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(true);
        commonResult.setCode("20000");
        commonResult.setMessage("成功");
        return commonResult;
    }

    //失败静态方法
    public static CommonResult error() {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(false);
        commonResult.setCode("20001");
        commonResult.setMessage("失败");
        return commonResult;
    }

    public CommonResult message(String message){
        this.setMessage(message);
        return this;
    }

    public CommonResult code(String code){
        this.setCode(code);
        return this;
    }

    public CommonResult code(ResultCode resultCode){
        this.setCode(resultCode.getError_code());
        this.setMessage(resultCode.getError_message());
        return this;
    }

    public CommonResult data(String key, Object value){
        Map map = new HashMap();
        map.put(key,value);
        T data= (T) map;
        this.setData(data);
        return this;
    }

    public CommonResult data(T data){
            this.setData(data);
            return this;
        }
}
