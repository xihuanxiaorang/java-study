package fun.xiaorang.springboot.stdresponseresult.enums;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/12 17:09
 */
public enum ResultCode implements IResultCode {
    SUCCESS(200, "请求成功"),
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_TYPE_ERROR(400, "参数类型不匹配"),
    NOT_FOUND(404, "资源不存在");


    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
