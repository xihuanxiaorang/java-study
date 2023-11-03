package fun.xiaorang.oss.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/3 2:43
 */
@Schema(name = "InitTaskReqDTO", description = "初始化任务请求参数")
@Data
@Accessors(chain = true)
public class InitTaskReqDTO {
    @Schema(description = "文件唯一标识（md5）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件唯一标识不能为空")
    private String identifier;

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @Schema(description = "文件大小（byte）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件大小不能为空")
    private long totalSize;

    @Schema(description = "分片大小（byte）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分片大小不能为空")
    private long chunkSize;
}
