package fun.xiaorang.spring.cloud.alibaba.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/7/21 13:02
 */
@RestController
@RequestMapping("/echo")
public class EchoController {
    private final RestTemplate restTemplate;

    public EchoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{name}")
    public String echo(@PathVariable String name) {
        URI uri = UriComponentsBuilder.fromHttpUrl("http://rest-provider/echo/{name}/1/{age}?sex={sex}&address={address}")
                .build(name, 28, "男", "长沙");
        String result = restTemplate.getForObject(uri, String.class);
        return "Hello RestTemplate：" + result;
    }
}
