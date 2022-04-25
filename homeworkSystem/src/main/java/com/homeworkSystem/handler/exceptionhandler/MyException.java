package com.homeworkSystem.handler.exceptionhandler;

import com.homeworkSystem.utils.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  //生成有参数构造方法
@NoArgsConstructor   //生成无参数构造
public class MyException extends RuntimeException {
    private String code;//状态码
    private String msg;//异常信息

    public MyException (ResultCode resultCode){
        this.code=resultCode.getError_code();
        this.msg=resultCode.getError_message();
    }
}
