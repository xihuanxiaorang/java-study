package fun.xiaorang.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">响应码枚举<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/20 3:03
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements BaseEnum {
    SUCCESS(200, "请求成功"),
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_TYPE_ERROR(400, "参数类型不匹配"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_ERROR(1001, "参数校验失败"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    ILLEGAL_OPERATION(1003, "非法操作"),
    ;

    private final Integer code;
    private final String name;
}
