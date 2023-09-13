package fun.xiaorang.oss.controller;

import fun.xiaorang.oss.core.OssTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-awesome">java-awesome</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/5/24 16:59
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/oss")
public class OssController {
    private final OssTemplate ossTemplate;

    /**
     * 根据指定的桶名称创建一个桶
     *
     * @param bucketName 桶名称
     */
    @PostMapping("/bucket/{bucketName}")
    public void createBucket(@PathVariable String bucketName) {
        ossTemplate.createBucket(bucketName);
    }

    /**
     * 根据指定的桶名称创建一个桶
     *
     * @param bucketName 桶名称
     */
    @PostMapping("/object/{bucketName}")
    public void upload(@PathVariable String bucketName, MultipartFile file) throws IOException {
        ossTemplate.putObject(bucketName, file.getOriginalFilename(), file.getInputStream());
    }
}
