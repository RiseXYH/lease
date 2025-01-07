package com.atguigu.lease.common.minio;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
//@ConfigurationPropertiesScan("com.atguigu.lease.common.minio")
public class MinioConfiguration {
/*//    引入yml文件内的minio配置信息
    @Value("${minio.endpoint}")
    private String endpoint;
    MinioClient.builder().endpoint(endpoint);*/

    @Autowired
    private MinioProperties minioProperties;
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minioProperties.getEndpoint())
                .endpoint(minioProperties.getAccessKey())
                .endpoint(minioProperties.getSecretKey())
                .build();
    }
}
