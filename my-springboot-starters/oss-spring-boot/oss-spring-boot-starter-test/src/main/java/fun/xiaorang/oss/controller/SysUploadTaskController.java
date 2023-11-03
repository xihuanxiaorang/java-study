package fun.xiaorang.oss.controller;

import fun.xiaorang.oss.enums.ResultCode;
import fun.xiaorang.oss.exception.BusinessException;
import fun.xiaorang.oss.model.dto.InitTaskReqDTO;
import fun.xiaorang.oss.model.po.SysUploadTask;
import fun.xiaorang.oss.model.vo.TaskInfoVO;
import fun.xiaorang.oss.service.SysUploadTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 分片上传-分片任务记录 前端控制器
 * </p>
 *
 * @author xiaorang
 * @since 2023-11-02
 */
@Tag(name = "分片上传")
@RequiredArgsConstructor
@RestController
@RequestMapping("/oss/tasks")
public class SysUploadTaskController {
    private final SysUploadTaskService sysUploadTaskService;

    @Operation(summary = "根据文件唯一标识（md5）获取分片上传任务信息")
    @GetMapping("/{identifier}")
    public TaskInfoVO taskInfo(@PathVariable("identifier") String identifier) {
        return sysUploadTaskService.getTaskInfo(identifier);
    }

    @Operation(summary = "初始化分片上传任务")
    @PostMapping
    public TaskInfoVO initTask(@Valid @RequestBody InitTaskReqDTO initTaskReqDTO) {
        return sysUploadTaskService.initTask(initTaskReqDTO);
    }

    @Operation(summary = "获取分片的预签名上传地址")
    @GetMapping("/{identifier}/{partNumber}")
    public String preSignUploadUrl(@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) {
        SysUploadTask sysUploadTask = sysUploadTaskService.getByIdentifier(identifier)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND.getCode(), "分片任务不存在！"));
        Map<String, String> params = new HashMap<>(2);
        params.put("uploadId", sysUploadTask.getUploadId());
        params.put("partNumber", partNumber.toString());
        return sysUploadTaskService.genPreSignUploadUrl(sysUploadTask.getBucketName(), sysUploadTask.getObjectKey(), params);
    }

    @Operation(summary = "合并分片")
    @PostMapping("/merge/{identifier}")
    public void merge(@PathVariable("identifier") String identifier) {
        sysUploadTaskService.merge(identifier);
    }
}
