package fun.xiaorang.spring.cloud.alibaba.rest.controller;

import fun.xiaorang.spring.cloud.alibaba.rest.common.pojo.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/21 14:09
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final List<User> users = Stream.of(
            new User(1, "xiaorang", 28),
            new User(2, "xiaobai", 18),
            new User(3, "xiaohei", 16)).collect(Collectors.toList());

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElseThrow(() -> new RuntimeException("哦吼~没有找到对应的用户信息！"));
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        users.add(user);
        return "添加成功！";
    }
}
