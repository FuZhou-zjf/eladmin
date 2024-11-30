package me.zhengjie.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.OssFile;
import me.zhengjie.domain.vo.OssConfigVO;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.OssFileRepository;
import me.zhengjie.service.OssService;
import me.zhengjie.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import me.zhengjie.config.OssConfiguration;

@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {
    
    private final OssConfiguration ossConfig;
    private final OssFileRepository ossFileRepository;
    
    @Override
    public OssConfigVO getOssConfig() {
        OssConfigVO config = new OssConfigVO();
        config.setEndpoint(ossConfig.getEndpoint());
        config.setAccessKeyId(ossConfig.getAccessKeyId());
        config.setAccessKeySecret(ossConfig.getAccessKeySecret());
        config.setBucketName(ossConfig.getBucketName());
        config.setDomain(ossConfig.getDomain());
        return config;
    }

    @Override
    public OssFile upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("上传文件不能为空");
        }
        if (file.getSize() > 100 * 1024 * 1024) {
            throw new BadRequestException("文件大小不能超过100MB");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (!isAllowedContentType(contentType)) {
            throw new BadRequestException("不支持的文件类型");
        }
        
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getExtensionName(originalFilename);
        String prefix = FileUtil.getFileNameNoEx(originalFilename);
        
        // 生成文件路径
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String objectName = String.format("upload/%s/%s-%s.%s", 
            dateStr, prefix, System.currentTimeMillis(), suffix);

        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), objectName, file.getInputStream());
            ossClient.putObject(putObjectRequest);
            
            // 保存文件记录
            OssFile ossFile = new OssFile();
            ossFile.setFileName(objectName);
            ossFile.setOriginalFileName(originalFilename);
            ossFile.setFileType(file.getContentType());
            ossFile.setFileSize(file.getSize());
            ossFile.setFilePath(objectName);
            ossFile.setFileUrl(ossConfig.getDomain() + "/" + objectName);
            
            return ossFileRepository.save(ossFile);
        } catch (IOException e) {
            throw new BadRequestException("上传失败：" + e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }
    
    private boolean isAllowedContentType(String contentType) {
        // 根据需求配置允许的文件类型
        return contentType != null && (
            contentType.startsWith("image/") ||
            contentType.startsWith("application/") ||
            contentType.startsWith("text/")
        );
    }

    @Override
    public Page<OssFile> query(String fileName, Pageable pageable) {
        return ossFileRepository.findAll((root, query, cb) -> {
            if (StringUtils.isNotBlank(fileName)) {
                return cb.like(root.get("fileName"), "%" + fileName + "%");
            }
            return null;
        }, pageable);
    }

    @Override
    public void delete(Long id) {
        OssFile ossFile = ossFileRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("文件不存在"));
            
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), 
            ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), ossFile.getFilePath());
            ossFileRepository.delete(ossFile);
        } finally {
            ossClient.shutdown();
        }
    }
} 