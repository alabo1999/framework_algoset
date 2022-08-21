package com.abc.example.exception;

/**
 * @className		: ExceptionCodes
 * @description	: 异常信息枚举类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public enum ExceptionCodes {
    // 0-99，reserved for common exception
    SUCCESS(0, "message.SUCCESS", "操作成功"),
    FAILED(1, "message.FAILED", "操作失败"),
    ERROR(99, "message.ERROR", "操作异常"),
    ARGUMENTS_ERROR(2, "message.ARGUMENTS_ERROR","参数错误"),

    // 鉴权
    TOKEN_IS_NULL(10, "message.TOKEN_IS_NULL","token不存在，请登录"),
    TOKEN_EXPIRED(11, "message.TOKEN_EXPIRED","token过期，请登录"),
    TOKEN_WRONG(12, "message.TOKEN_WRONG","token不正确，请登录"),
    SESSION_DATA_WRONG(13, "message.SESSION_DATA_WRONG","缓存数据异常，请重新登录"),
    ACCESS_FORBIDDEN(14, "message.ACCESS_FORBIDDEN","禁止访问"),
    NO_RIGHTS(15, "message.NO_RIGHTS","无权操作"),
    TASKID_NOT_EXIST(16, "message.TASKID_NOT_EXIST","任务ID不存在，可能已过期销毁"),
    TASKID_NOT_RIGHTS(17, "message.TASKID_NOT_RIGHTS","无权访问此任务ID"),
    SESSION_IS_NULL(18, "message.SESSION_IS_NULL","session为空，请重新登录"),
        
    // 数据格式
    DATE_FORMAT_WRONG(20, "message.DATE_FORMAT_WRONG", "日期格式错误"),
    DB_DATA_WRONG(21, "message.DB_DATA_WRONG", "数据库中数据错误"),
    ARGUMENTS_IS_EMPTY(22, "message.ARGUMENTS_IS_EMPTY","参数值不能为空"),
    ARGUMENTS_IS_INCOMPATIBLE(23, "message.ARGUMENTS_IS_INCOMPATIBLE","参数值不一致"),
    
    // 数据库操作
    ADD_OBJECT_FAILED(30, "message.ADD_OBJECT_FAILED", "新增对象失败"),
    UPDATE_OBJECT_FAILED(31, "message.UPDATE_OBJECT_FAILED", "修改对象失败"),
    DELETE_OBJECT_FAILED(32, "message.DELETE_OBJECT_FAILED", "删除对象失败"),
    QUERY_OBJECT_FAILED(33, "message.QUERY_OBJECT_FAILED", "查询对象失败"),
    UNIQUE_KEY_FAILED(34, "message.UNIQUE_KEY_FAILED", "唯一键重复"),
    OBJECT_DOES_NOT_EXIST(35, "message.OBJECT_DOES_NOT_EXIST", "指定对象不存在"),
    QUERY_RESULT_IS_EMPTY(36, "message.QUERY_RESULT_IS_EMPTY", "查询结果为空集"),
    OBJECT_IS_REMOVED(37, "message.OBJECT_IS_REMOVED", "指定对象已禁用"),
    OBJECT_ALREADY_EXISTS(38, "message.OBJECT_ALREADY_EXISTS", "指定对象已存在"),
    OBJECT_BE_LOCKED(39, "message.OBJECT_BE_LOCKED", "指定对象被锁定，禁止修改"),
    
    // 文件上传
    UPLOAD_NULL_FILE(40, "message.UPLOAD_NULL_FILE", "上传文件为空"),
    UPLOAD_WRONG_FILE_FORMAT(41, "message.UPLOAD_WRONG_FILE_FORMAT", "上传文件格式错误"),
    UPLOAD_NEED_EXCEL(42, "message.UPLOAD_NEED_EXCEL", "上传文件格式错误，需要.xls/.xlsx格式文件"),
    UPLOAD_READFILE_FAILED(43, "message.UPLOAD_READFILE_FAILED", "获取上传文件失败"),
    UPLOAD_READ_EXCEL_FILE_FAILED(44, "message.UPLOAD_READ_EXCEL_FILE_FAILED", "打开EXCEL文件失败"),
    UPLOAD_EXCEL_MISSING_KEY_COLUMN(45, "message.UPLOAD_EXCEL_MISSING_KEY_COLUMN", "excel文件标题行缺失关键列："),
    UPLOAD_EXCEL_DATATYPE_WRONG(46, "message.UPLOAD_EXCEL_DATATYPE_WRONG", "数据格读取失败，可能类型转换失败，(行，列)："),
    UPLOAD_FILE_NO_DATA(47, "message.UPLOAD_FILE_NO_DATA", "上传文件无数据"),
    UPLOAD_WITH_WRONG_DATA(48, "message.UPLOAD_WITH_WRONG_DATA", "部分数据异常"),
    
    // 读取文件
    READFILE_FAILED(50, "message.READFILE_FAILED", "读取文件失败"),
    COPYFILE_FAILED(51, "message.COPYFILE_FAILED", "复制文件失败"),
    CLOSEFILE_FAILED(52, "message.CLOSEFILE_FAILED", "关闭文件失败"),
    DELETEFILE_FAILED(53, "message.DELETEFILE_FAILED", "删除文件失败"),
    // 文件下载
    EXPORT_EXCEL_FILE_FAILED(55, "message.EXPORT_EXCEL_FILE_FAILED", "导出Excel文件失败"),
    DOWNLOAD_FILE_FAILED(56, "message.DOWNLOAD_FILE_FAILED", "下载文件失败"),
    EXPORT_FILE_NO_DATA(57, "message.EXPORT_FILE_NO_DATA", "无数据可导出"),
    ZIP_FILE_FAILED(58, "message.ZIP_FILE_FAILED", "Zip压缩文件失败"),

    // 变更通知信息
    USER_RIGHTS_CHANGED(60, "message.USER_RIGHTS_CHANGED", "用户权限发生变更"),
    
    // 刷新系统数据
    RELOAD_FAILED(70, "message.RELOAD_FAILED", "重新加载%s数据失败"),
        
    // 反射调用异常
    No_Such_Field_Exception(80, "message.No_Such_Field_Exception", "无此字段异常"),
    EXPR_SYNTAX_ERROR(81, "message.EXPR_SYNTAX_ERROR", "Syntax error"),
        
    // 100-199，登录模块
    LOGINNAME_PASSWORD_WRONG(100, "message.LOGINNAME_PASSWORD_WRONG", "用户名或密码错误"),
    VERIFYCODE_WRONG(101, "message.VERIFYCODE_WRONG", "验证码错误"),
    LOGINNAME_IS_EMPTY(102, "message.LOGINNAME_IS_EMPTY", "用户名不能为空"),
    LOGINNAME_FORMAT_ERROR(103, "message.LOGINNAME_FORMAT_ERROR", "用户名格式错误，字符为数字、字母、空格、'.-_@'"),
    PASSWORD_IS_EMPTY(104, "message.PASSWORD_IS_EMPTY", "密码不能为空"),
    VERIFYCODE_IS_EMPTY(105, "message.VERIFYCODE_IS_EMPTY", "验证码不能为空"),
    VERIFYCODE_OUTPUT_FAILED(106, "message.VERIFYCODE_OUTPUT_FAILED", "获取验证码失败"),
    PASSWORD_WRONG(107, "message.PASSWORD_WRONG", "原密码错误"),
    
    // 300-399，用户管理模块
    USERID_IS_EMPTY(300, "message.USERID_IS_EMPTY", "用户ID不能为空"),
    USERNAME_IS_EMPTY(301, "message.USERNAME_IS_EMPTY", "用户名称不能为空"),
    USERTYPE_IS_WRONG(303, "message.USERTYPE_IS_WRONG", "用户类型值错误"),
    PHONENUMBER_IS_EMPTY(304, "message.PHONENUMBER_IS_EMPTY", "手机号不能为空"),
    DRTYPE_IS_WRONG(305, "message.DRTYPE_IS_WRONG", "用户数据权限类型值错误"),
    DRTYPE_IS_UNSUPPORT(306, "message.DRTYPE_IS_UNSUPPORT", "用户数据权限类型值不支持此操作"),
    PASSWORD_IS_INCONSISTENT(307, "message.PASSWORD_IS_INCONSISTENT", "确认密码不一致"),
    NO_ROLE_ASSIGNED(308, "message.NO_ROLE_ASSIGNED", "该用户未分配角色"),

    KEYINFO_IS_EXIST(310, "message.KEYINFO_IS_EXIST", "关键信息已存在"),

    // 400-499，组织管理模块
    ORGID_IS_INVALID(400, "message.ORGID_IS_INVALID", "组织ID无效"),
    ORGTYPE_IS_INVALID(401, "message.ORGTYPE_IS_INVALID", "组织类型不支持此操作"),    
    
    ;	// 定义结束
	
	// 返回码
    private int code;
    public int getCode() {
    	return this.code;
    }
    
    // 返回消息ID
    private String messageId;
    public String getMessageId() {
    	return this.messageId;
    }

    // 返回消息
    private String message;
    public String getMessage() {
    	return this.message;
    }
    
    ExceptionCodes(int code, String messageId, String message) {
        this.code = code;
        this.messageId = messageId;
        this.message = message;
    }
}
