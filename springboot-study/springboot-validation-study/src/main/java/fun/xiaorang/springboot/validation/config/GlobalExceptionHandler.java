package fun.xiaorang.springboot.validation.config;

import fun.xiaorang.springboot.validation.enums.ResultCode;
import fun.xiaorang.springboot.validation.exception.BusinessException;
import fun.xiaorang.springboot.validation.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

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
        return ResponseEntity.badRequest()
                .body(Result.fail(ResultCode.PARAM_TYPE_ERROR));
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Result<Void>> handleException(ArithmeticException e) {
        log.error("算术异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(Result.fail(ResultCode.SYSTEM_ERROR));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result<Void>> handleException(NoHandlerFoundException e) {
        log.error("404异常信息，异常堆栈信息：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Result.fail(ResultCode.NOT_FOUND));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleException(BindException e) {
        log.error("参数校验异常信息，异常堆栈信息：{}", e.getMessage(), e);
        String msg = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return ResponseEntity.badRequest()
                .body(Result.fail(ResultCode.VALIDATION_ERROR.getCode(), msg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleException(ConstraintViolationException e) {
        log.error("参数校验异常信息，异常堆栈信息：{}", e.getMessage(), e);
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return ResponseEntity.badRequest()
                .body(Result.fail(ResultCode.VALIDATION_ERROR.getCode(), msg));
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
