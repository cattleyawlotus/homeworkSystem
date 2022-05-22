package com.homeworkSystem.utils;


import lombok.Getter;

@Getter
public enum ResultCode {
    /**
     * 错误码定义
     **/
    BadRequest                  ("400", "请求有误！"),
    Unauthorized                ("401", "用户未登录！"),
    ExpiredToken                ("402", "用户登录状态已过期，请重新登录！"),
    Forbidden                   ("403", "用户无权限！"),
    NotFound                    ("404", "找不到请求地址！"),
    InvalidToken                ("405", "token校验失败，token可能遭到篡改！"),
    InvalidTokenFormat          ("406", "token格式错误，请检查token格式！"),
    ParseTokenError             ("407", "解析token时发生错误，请检查token！"),
    MismatchedToken             ("408", "当前的token与签发的token不一致，请检查token或者重新登录！"),
    NoLoginState                ("409", "查询不到用户的登陆信息，请重新登录！"),
    MissingRequestParameter     ("410", "请求参数缺失，请检查接口要求的请求参数是否完整！"),
    RequestMethodNotSupported   ("411", "该接口不支持此请求方法，请检查http请求方法是否正确！"),
    HttpMediaTypeNotSupported   ("412", "该接口不支持此请求数据类型，请检查http请求头的Content-Type字段是否正确！"),
    InternalServerError         ("500", "内部服务器错误！"),
    NullPointerError            ("501", "服务器发生空指针异常！"),
    InsertError                 ("801","新增错误！"),
    UpdateError                 ("802","更新错误！"),
    DeleteError                 ("803","删除失败！"),
    DuplicateKeyError           ("804","更新失败，请检查数据主键是否重复！"),
    ConnectionError             ("806","连接失败"),

    //用户模块业务逻辑错误
    UserLoginVerifyFail         ("01001","用户名或密码错误"),
    UserUnLogin                 ("01002","用户未登录"),
    AddUserFail                 ("01003","添加用户失败"),
    OldPasswordUnMatch          ("01004","原始密码不匹配"),
    ModifyPasswordFail          ("01005","修改密码失败"),
    DeleteUserFail              ("01006","删除用户失败"),
    UserNotExist                ("01007","用户不存在"),
    UserAlreadyExist            ("01008","用户已存在"),
    VerificationError           ("01009","验证码错误"),
    VerificationCodeExpired     ("01010","验证码失效"),
    PhoneNumberError            ("01011","手机号格式错误"),
    UploadImageError            ("01012","上传文件失败"),
    BannedUser                  ("01013", "用户已被禁用"),
    AddAdminFail                ("01014","添加管理员失败"),
    AdminAlreadyExist           ("01015","管理员已存在"),
    NotStudent                  ("01016","用户不为学生"),


    ThumbsAlready               ("02001", "已点赞"),
    FatherCommentError          ("02002", "父评论错误"),
    TypeExsist                  ("02003","类型已存在"),
    TypeNotExsist               ("02004","类型不存在"),
    TagExsist                   ("02005","标签已存在"),
    TagNotExsist                ("02006","标签不存在"),
    CommentNotExsist            ("02007","评论不存在"),
    CollectsAlready             ("02008", "已收藏"),

    HomeworkNotExsist           ("03001","作业不存在"),
    StudentClassNotMatch        ("03002","该学生不在班级内"),
    StudentNotSubmit            ("03003","该学生尚未提交本次作业");
    private final String error_code;

    private final String error_message;

    ResultCode(String errorCode, String errorMessage) {
        this.error_code = errorCode;
        this.error_message = errorMessage;
    }

}
