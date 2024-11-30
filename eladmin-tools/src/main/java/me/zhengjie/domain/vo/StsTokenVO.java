package me.zhengjie.domain.vo;

import lombok.Data;

@Data
public class StsTokenVO {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
} 