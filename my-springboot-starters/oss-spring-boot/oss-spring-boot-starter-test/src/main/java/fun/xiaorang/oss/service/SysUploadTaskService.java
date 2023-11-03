package fun.xiaorang.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.xiaorang.oss.model.dto.InitTaskReqDTO;
import fun.xiaorang.oss.model.po.SysUploadTask;
import fun.xiaorang.oss.model.vo.TaskInfoVO;

import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 分片上传-分片任务记录 服务类
 * </p>
 *
 * @author xiaorang
 * @since 2023-11-02
 */
public interface SysUploadTaskService extends IService<SysUploadTask> {
    /**
     * 根据文件唯一标识获取分片上传任务信息
     *
     * @param identifier 文件唯一标识（md5）
     * @return 任务信息
     */
    TaskInfoVO getTaskInfo(String identifier);

    /**
     * 根据文件唯一标识（md5）获取分片上传任务
     *
     * @param identifier 文件唯一标识（md5）
     * @return 分片上传任务
     */
    Optional<SysUploadTask> getByIdentifier(String identifier);

    /**
     * 初始化分片上传任务
     *
     * @param initTaskReqDTO 初始化分片上传任务请求参数
     * @return 任务信息
     */
    TaskInfoVO initTask(InitTaskReqDTO initTaskReqDTO);

    /**
     * 生成分片的预签名上传地址
     *
     * @param bucketName 桶名称
     * @param objectKey  对象名称
     * @param params     额外参数
     * @return 预签名上传地址
     */
    String genPreSignUploadUrl(String bucketName, String objectKey, Map<String, String> params);

    /**
     * 合并分片
     *
     * @param identifier 文件唯一标识（md5）
     */
    void merge(String identifier);
}
