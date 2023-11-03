package fun.xiaorang.oss.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/2 23:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TaskInfoVO {
    /**
     * 是否完成上传（是否已经合并分片）
     */
    private boolean finished;
    /**
     * 文件地址
     */
    private String path;
    /**
     * 上传记录
     */
    private TaskRecordVO taskRecord;
}
