package fun.xiaorang.springboot.validation.pojo;

import fun.xiaorang.springboot.validation.enums.IResultCode;
import fun.xiaorang.springboot.validation.enums.ResultCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/12 12:19
 */
@Data
@Builder
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMsg())
                .data(data)
                .build();
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return Result.<T>builder().success(false).code(code).msg(msg).build();
    }

    public static <T> Result<T> fail(IResultCode resultCode) {
        return Result.<T>builder()
                .success(false)
                .code(resultCode.getCode())
                .msg(resultCode.getMsg())
                .build();
    }
}
