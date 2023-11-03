package fun.xiaorang.oss.model.vo;

import com.amazonaws.services.s3.model.PartSummary;
import fun.xiaorang.oss.model.po.SysUploadTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/2 23:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskRecordVO extends SysUploadTask {
    /**
     * 已上传完的分片
     */
    private List<PartSummary> existPartList;
}
