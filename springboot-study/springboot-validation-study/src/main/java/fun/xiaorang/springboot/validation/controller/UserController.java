package fun.xiaorang.springboot.validation.controller;

import fun.xiaorang.springboot.validation.entity.User;
import fun.xiaorang.springboot.validation.validation.Insert;
import fun.xiaorang.springboot.validation.validation.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/10/13 10:12
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @PostMapping
    public User create(@Validated(Insert.class) @RequestBody User user) {
        // 此处只会验证 Insert 分组下的约束注解
        return user;
    }

    @PutMapping
    public User save(@Validated(Update.class) @RequestBody User user) {
        // 此处只会验证 Update 分组下的约束注解
        return user;
    }

    @GetMapping("echo")
    public User query(@NotBlank(message = "用户名不能为空") @RequestParam String username,
                      @Min(value = 0, message = "年龄最小为{value}") @RequestParam Integer age) {
        return User.builder().id(1L).username(username).password("123456").age(age).email("15019474951@163.com").build();
    }
}
