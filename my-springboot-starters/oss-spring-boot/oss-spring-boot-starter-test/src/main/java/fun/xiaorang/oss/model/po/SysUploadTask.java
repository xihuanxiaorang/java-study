package fun.xiaorang.oss.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 分片上传-分片任务记录
 * </p>
 *
 * @author xiaorang
 * @since 2023-11-02
 */
@Schema(name = "SysUploadTask", description = "分片上传-分片任务记录")
@Data
@TableName("sys_upload_task")
@Accessors(chain = true)
public class SysUploadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "分片上传的uploadId")
    private String uploadId;

    @Schema(description = "文件唯一标识（md5）")
    private String fileIdentifier;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "所属桶名")
    private String bucketName;

    @Schema(description = "文件的key")
    private String objectKey;

    @Schema(description = "文件大小（byte）")
    private Long totalSize;

    @Schema(description = "每个分片大小（byte）")
    private Long chunkSize;

    @Schema(description = "分片数量")
    private Integer chunkNum;
}
