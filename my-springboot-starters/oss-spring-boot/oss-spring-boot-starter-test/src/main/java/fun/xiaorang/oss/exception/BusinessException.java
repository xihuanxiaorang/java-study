package fun.xiaorang.oss.exception;

import fun.xiaorang.oss.enums.BaseEnum;
import lombok.Getter;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; ">业务异常<p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/20 3:05
 */
@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;
    private final String msg;

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(BaseEnum resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getName();
    }
}
