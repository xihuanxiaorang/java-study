package fun.xiaorang.oss.converter;

import fun.xiaorang.oss.model.po.SysUploadTask;
import fun.xiaorang.oss.model.vo.TaskRecordVO;
import org.mapstruct.Mapper;

/**
 * @author liulei
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://blog.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2023/11/3 2:11
 */
@Mapper
public interface SysUploadTaskConverter {
    TaskRecordVO convert(SysUploadTask sysUploadTask);
}
