package me.zhengjie.domain.vo;

import lombok.Data;

@Data
public class OssConfigVO {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String domain;
} 