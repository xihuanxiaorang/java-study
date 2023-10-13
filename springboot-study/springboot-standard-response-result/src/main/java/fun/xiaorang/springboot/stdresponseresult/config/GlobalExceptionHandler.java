package fun.xiaorang.springboot.stdresponseresult.config;

import fun.xiaorang.springboot.stdresponseresult.enums.ResultCode;
import fun.xiaorang.springboot.stdresponseresult.exception.BusinessException;
import fun.xiaorang.springboot.stdresponseresult.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/12 18:10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Result.fail(ResultCode.PARAM_TYPE_ERROR));
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Result<Void>> handleException(ArithmeticException e) {
        log.error("算术异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(ResultCode.SYSTEM_ERROR));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Void>> handleException(NoHandlerFoundException e) {
        log.error("404异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.fail(ResultCode.NOT_FOUND));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleException(BusinessException e) {
        log.error("业务异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(e.getCode(), e.getMsg()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("全局异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(ResultCode.SYSTEM_ERROR));
    }
}
