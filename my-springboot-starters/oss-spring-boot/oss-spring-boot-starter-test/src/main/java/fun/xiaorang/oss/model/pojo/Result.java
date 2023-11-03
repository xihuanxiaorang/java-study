package fun.xiaorang.oss.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import fun.xiaorang.oss.enums.BaseEnum;
import fun.xiaorang.oss.enums.ResultCode;
import lombok.Builder;
import lombok.Data;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">返回结果类<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/20 2:59
 */
@Data
@Builder
public class Result<T> {
    private Boolean success;
    private Integer code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getName())
                .data(data)
                .build();
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return Result.<T>builder().success(false).code(code).msg(msg).build();
    }

    public static <T> Result<T> fail(BaseEnum resultCode) {
        return Result.<T>builder()
                .success(false)
                .code(resultCode.getCode())
                .msg(resultCode.getName())
                .build();
    }
}
