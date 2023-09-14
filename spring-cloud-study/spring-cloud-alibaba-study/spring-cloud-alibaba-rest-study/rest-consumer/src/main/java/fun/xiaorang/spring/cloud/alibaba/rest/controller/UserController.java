package fun.xiaorang.spring.cloud.alibaba.rest.controller;

import fun.xiaorang.spring.cloud.alibaba.rest.common.pojo.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/21 13:02
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://rest-provider/user/{id}").build(id);
        return restTemplate.getForObject(uri, User.class);
    }

    @GetMapping
    public List<User> getUsers() {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://rest-provider/user").build().toUri();
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        });
        return responseEntity.getBody();
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://rest-provider/user").build().toUri();
        return restTemplate.postForObject(uri, user, String.class);
    }
}

