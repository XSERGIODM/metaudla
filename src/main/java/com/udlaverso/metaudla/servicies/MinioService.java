package com.udlaverso.metaudla.servicies;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void createBucket(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating bucket: " + bucketName, e);
        }
    }

    public void uploadFile(String bucketName, String objectName, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + objectName, e);
        }
    }

    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + objectName, e);
        }
    }

    public List<String> listObjects(String bucketName, String prefix) {
        List<String> objectNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                objectNames.add(item.objectName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error listing objects in bucket: " + bucketName, e);
        }
        return objectNames;
    }

    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + objectName, e);
        }
    }

    public String getPresignedUrl(String bucketName, String objectName, int expirySeconds) {
        try {
            // Validar que el objeto existe antes de generar la URL
            minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );

            // Limitar la expiración a un máximo de 7 días (604800 segundos) para evitar errores de MinIO
            int maxExpirySeconds = 604800; // 7 días
            int actualExpirySeconds = Math.min(expirySeconds, maxExpirySeconds);

            log.debug("Generating presigned URL for bucket: {}, object: {}, expiry: {} seconds", bucketName, objectName, actualExpirySeconds);

            String presignedUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(actualExpirySeconds, TimeUnit.SECONDS)
                    .build()
            );

            log.debug("Presigned URL generated successfully for: {}/{}", bucketName, objectName);
            return presignedUrl;

        } catch (Exception e) {
            log.error("Error generating presigned URL for: {}/{} - Cause: {}", bucketName, objectName, e.getMessage(), e);
            throw new RuntimeException("Error generating presigned URL for: " + bucketName + "/" + objectName + ". Cause: " + e.getMessage(), e);
        }
    }
}