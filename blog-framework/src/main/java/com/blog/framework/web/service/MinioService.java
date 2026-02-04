package com.blog.framework.web.service;

import com.blog.common.exception.file.FileSizeLimitExceededException;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.file.FileUploadUtils;
import com.blog.framework.config.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author 31373
 */
@Service
public class MinioService {
    public static final String DEFAULT_DELIMITER = "/";

    private final MinioClient minioClient;

    private final MinioConfig minioConfig;

    public MinioService(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
    }

    /**
     * 初始化存储桶
     */
    public void initBucket() throws Exception {
        if (!bucketExists(minioConfig.getBucketName())) {
            // 创建存储桶
            makeBucket(minioConfig.getBucketName());
        }
    }


    /**
     * 创建存储桶
     */
    public void makeBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build());
    }

    /**
     * 判断存储桶是否存在
     */
    public boolean bucketExists(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 上传图片
     *
     * @param file             图片文件
     * @param allowedExtension 允许的文件后缀
     * @param useCustomNaming  是否使用自定义命名
      * @return 图片路径
     */
    public String uploadImage(MultipartFile file, String[] allowedExtension, boolean useCustomNaming) throws Exception {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileSizeLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        // 校验文件后缀
        FileUploadUtils.assertAllowed(file, allowedExtension);
        String fileName = useCustomNaming ? FileUploadUtils.uuidFilename(file) : FileUploadUtils.extractFilename(file);
        //存储桶的名称
        String bucketName = minioConfig.getBucketName();
        // 确保存储桶存在
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        // 返回可访问的URL
        return minioConfig.getEndpoint() + DEFAULT_DELIMITER + minioConfig.getBucketName() + DEFAULT_DELIMITER + fileName;
    }

    public void deleteFile(String oldAvatar) throws Exception {
        if (StringUtils.isNotEmpty(oldAvatar)) {
            String fileName = stripPrefix(oldAvatar);
            if (StringUtils.isNotEmpty(fileName)) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(fileName)
                                .build()
                );
            }
        }
    }

    public String stripPrefix(String url) {
        //需要去除的前缀
        String prefix = minioConfig.getEndpoint() + DEFAULT_DELIMITER + minioConfig.getBucketName() + DEFAULT_DELIMITER;
        return StringUtils.substringAfter(url, prefix);
    }
}
