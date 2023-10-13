package fun.xiaorang.springboot.stdresponseresult.controller;

import fun.xiaorang.springboot.stdresponseresult.enums.ResultCode;
import fun.xiaorang.springboot.stdresponseresult.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/12 12:19
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/success")
    public Map<String, Object> success() {
        Integer age = 18;
        String name = "小让";
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        return map;
    }

    @GetMapping("/error")
    public Result error() {
        int i = 1 / 0;
        return Result.fail(ResultCode.SYSTEM_ERROR);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/error2")
    public Integer error2(Integer age) {
        return age;
    }
}
