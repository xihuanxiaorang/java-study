package fun.xiaorang.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.PartSummary;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xiaorang.oss.converter.SysUploadTaskConverter;
import fun.xiaorang.oss.core.OssTemplate;
import fun.xiaorang.oss.mapper.SysUploadTaskMapper;
import fun.xiaorang.oss.model.dto.InitTaskReqDTO;
import fun.xiaorang.oss.model.po.SysUploadTask;
import fun.xiaorang.oss.model.vo.TaskInfoVO;
import fun.xiaorang.oss.properties.OssProperties;
import fun.xiaorang.oss.service.SysUploadTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 分片上传-分片任务记录 服务实现类
 * </p>
 *
 * @author xiaorang
 * @since 2023-11-02
 */
@RequiredArgsConstructor
@Service
public class SysUploadTaskServiceImpl extends ServiceImpl<SysUploadTaskMapper, SysUploadTask> implements SysUploadTaskService {
    private final SysUploadTaskConverter sysUploadTaskConverter;
    private final OssTemplate ossTemplate;
    private final OssProperties ossProperties;

    @Override
    public TaskInfoVO getTaskInfo(String identifier) {
        Optional<SysUploadTask> sysUploadTaskOptional = this.getByIdentifier(identifier);
        if (sysUploadTaskOptional.isEmpty()) {
            return null;
        }
        SysUploadTask sysUploadTask = sysUploadTaskOptional.get();
        String bucketName = sysUploadTask.getBucketName();
        String objectKey = sysUploadTask.getObjectKey();
        TaskInfoVO taskInfoVO = new TaskInfoVO()
                .setFinished(true)
                .setPath(ossTemplate.getPath(bucketName, objectKey))
                .setTaskRecord(sysUploadTaskConverter.convert(sysUploadTask));
        boolean existed = ossTemplate.doesObjectExist(bucketName, objectKey);
        if (!existed) {
            // 未上传完，返回已上传的分片
            String uploadId = sysUploadTask.getUploadId();
            PartListing partListing = ossTemplate.listParts(uploadId, bucketName, objectKey);
            taskInfoVO.setFinished(false).getTaskRecord().setExistPartList(partListing.getParts());
        }
        return taskInfoVO;
    }

    @Override
    public Optional<SysUploadTask> getByIdentifier(String identifier) {
        return Optional.ofNullable(this.getOne(Wrappers.<SysUploadTask>lambdaQuery().eq(SysUploadTask::getFileIdentifier, identifier)));
    }

    @Transactional
    @Override
    public TaskInfoVO initTask(InitTaskReqDTO initTaskReqDTO) {
        String fileName = initTaskReqDTO.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String objectKey = StrUtil.format("{}/{}.{}", DateUtil.format(LocalDateTime.now(), "YYYY/MM/dd"), IdUtil.randomUUID(), suffix);
        String mediaType = MediaTypeFactory.getMediaType(objectKey).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        String bucketName = ossProperties.getBucketName();
        int chunkNum = (int) Math.ceil(initTaskReqDTO.getTotalSize() * 1.0 / initTaskReqDTO.getChunkSize());
        InitiateMultipartUploadResult initiateMultipartUploadResult = ossTemplate.initiateMultipartUpload(bucketName, objectKey, mediaType);
        String uploadId = initiateMultipartUploadResult.getUploadId();
        SysUploadTask sysUploadTask = new SysUploadTask()
                .setUploadId(uploadId)
                .setFileIdentifier(initTaskReqDTO.getIdentifier())
                .setFileName(fileName)
                .setBucketName(bucketName)
                .setObjectKey(objectKey)
                .setTotalSize(initTaskReqDTO.getTotalSize())
                .setChunkSize(initTaskReqDTO.getChunkSize())
                .setChunkNum(chunkNum);
        this.save(sysUploadTask);
        return new TaskInfoVO()
                .setFinished(false)
                .setPath(ossTemplate.getPath(bucketName, objectKey))
                .setTaskRecord(sysUploadTaskConverter.convert(sysUploadTask));
    }

    @Override
    public String genPreSignUploadUrl(String bucketName, String objectKey, Map<String, String> params) {
        return ossTemplate.getPutObjectUrl(bucketName, objectKey, params).toString();
    }

    @Override
    public void merge(String identifier) {
        SysUploadTask sysUploadTask = this.getByIdentifier(identifier).orElseThrow(() -> new RuntimeException("分片任务不存在！"));
        String uploadId = sysUploadTask.getUploadId();
        String bucketName = sysUploadTask.getBucketName();
        String objectKey = sysUploadTask.getObjectKey();
        PartListing partListing = ossTemplate.listParts(uploadId, bucketName, objectKey);
        List<PartSummary> parts = partListing.getParts();
        if (!sysUploadTask.getChunkNum().equals(parts.size())) {
            // 已上传分块数量与记录中的数量不对应，不能合并分块
            throw new RuntimeException("分片缺失，请重新上传！");
        }
        ossTemplate.completeMultipartUpload(uploadId, bucketName, objectKey, parts);
    }
}
