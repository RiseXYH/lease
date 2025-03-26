package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient client;

    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(properties.getBucketName()).build());
        if (!bucketExists) {
            // 创建minio存储桶
            client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucketName()).build());
            // 设置存储桶访问权限
            client.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(properties.getBucketName())
                    .config(createBucketPolicyConfig(properties.getBucketName()))
                    .build());
        }
        //上传文件的名字
        String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        //参数的作用：存储桶名称，文件名称，文件流，文件大小，文件类型
        client.putObject(PutObjectArgs.builder().
                bucket(properties.getBucketName()).
                object(filename).
                stream(file.getInputStream(), file.getSize(), -1).
                contentType(file.getContentType())
                .build());

        return String.join("/", properties.getEndpoint(), properties.getBucketName(), filename);


    }

    private String createBucketPolicyConfig(String bucketName) {
        //允许所有人访问
        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}


