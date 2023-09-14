package fun.xiaorang.spring.cloud.alibaba.rest.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/21 13:03
 */
@RestController
@RequestMapping("/echo")
public class EchoController {
    @GetMapping("/{name}/1/{age}")
    public String echo(@PathVariable String name, @PathVariable Integer age, @RequestParam String sex, @RequestParam String address) {
        return name + "-" + age + "-" + sex + "-" + address;
    }
}
