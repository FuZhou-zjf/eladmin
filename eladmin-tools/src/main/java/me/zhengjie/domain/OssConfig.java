package me.zhengjie.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String host;

    @NestedConfigurationProperty
    private StsConfig sts;

    @Data
    public static class StsConfig {
        private String roleArn;
        private String roleSessionName;
        private Long durationSeconds;
        private String policy;
    }
}
