package com.atguigu.lease.common.minio;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
//    映射yml文件的minio配置
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
