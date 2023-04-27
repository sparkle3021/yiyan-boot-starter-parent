package com.oho.oss.minio.core.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.oho.common.utils.CollectionUtils;
import com.oho.common.utils.StringUtils;
import com.oho.common.utils.ValidationUtils;
import com.oho.oss.minio.autoconfigure.properties.MinioProperties;
import com.oho.oss.minio.autoconfigure.properties.NginxProperties;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * minio工具类
 *
 * @author Sparkler
 */
@Slf4j
@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private NginxProperties nginxProperties;

    @Autowired
    private MinioProperties minioProperties;

    /**
     * 默认url过期时间
     */
    public static final int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;

    /**
     * 默认最大文件上传为500M
     */
    public static final int MAX_UPLOAD_FILE_SIZE = 1024 * 1024 * 500;

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        );
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            MakeBucketArgs.builder().bucket(bucketName).build();
        }
    }

    /**
     * 列出所有存储桶
     *
     * @return list
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 列出所有存储桶名称
     *
     * @return list
     */
    @SneakyThrows
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        return CollectionUtils.isNotEmpty(bucketList) ?
                bucketList.stream().map(Bucket::name).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @SneakyThrows
    public boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            // 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);
            return !flag;
        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return list
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return iterable
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        }
        return null;
    }

    /**
     * 获取文件md5
     *
     * @param stream the stream
     * @return file md 5
     */
    public String getFileMd5(InputStream stream) {
        return MD5.create().digestHex(stream);
    }

    /**
     * 获取文件md5
     *
     * @param multipartFile the multipart file
     * @return file md 5
     */
    @SneakyThrows
    public String getFileMd5(MultipartFile multipartFile) {
        return this.getFileMd5(multipartFile.getInputStream());
    }

    /**
     * 文件上传
     *
     * @param bucketName    the bucket name
     * @param multipartFile the multipart file
     * @return the string
     */
    @SneakyThrows
    public String putObject(String bucketName, MultipartFile multipartFile) {
        ValidationUtils.isTrue(multipartFile.getSize() <= minioProperties.getMaxUploadFileSize(), "minio.upload.file.is.too.big");
        ValidationUtils.isTrue(bucketExists(bucketName), "minio.bucket.is.not.exist");
        String objectName = this.getFileMd5(multipartFile);
        return this.putObject(bucketName, multipartFile.getInputStream(), objectName, multipartFile.getContentType());
    }

    /**
     * 通过InputStream上传对象
     *
     * @param bucketName  存储桶名称
     * @param stream      要上传的流
     * @param objectName  minio中文件名：取MD5
     * @param contentType 文件类型
     * @return string
     */
    @SneakyThrows
    public String putObject(String bucketName, InputStream stream, String objectName, String contentType) {
        ValidationUtils.isTrue(bucketExists(bucketName), "minio.bucket.is.not.exist");
        ValidationUtils.isTrue(StringUtils.isNotBlank(objectName), "minio.objectName.is.not.exist");
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .contentType(contentType)
                        .stream(stream, stream.available(), -1)
                        .build()
        );
        return objectWriteResponse.object();
    }

    /**
     * 以流的形式获取一个文件对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return object
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                return minioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
                );
            }
        }
        return null;
    }

    /**
     * 以流的形式获取一个文件对象（断点下载）
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return object
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                return minioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build()
                );
            }
        }
        return null;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return the boolean
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }
        return false;
    }

    /**
     * 删除指定桶的多个文件对象,返回删除错误的对象列表，全部删除成功，返回空列表
     *
     * @param bucketName  存储桶名称
     * @param objectNames 含有要删除的多个object名称的迭代器对象
     * @return list
     */
    @SneakyThrows
    public List<String> removeObject(String bucketName, List<String> objectNames) {
        ValidationUtils.isTrue(CollectionUtils.isNotEmpty(objectNames), "minio.delete.object.name.can.not.empty");
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<DeleteObject> objects = objectNames.stream().map(DeleteObject::new).collect(Collectors.toList());
            Iterable<Result<DeleteError>> results = minioClient
                    .removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
        }
        return deleteErrorNames;
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return string
     */
    @SneakyThrows
    public String preSignedGetObject(String bucketName, String objectName, Integer expires) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(Objects.isNull(expires) ? DEFAULT_EXPIRY_TIME : expires)
                    .build()
            );
        }
        if (minioProperties.getNginxLoadUrlEnable() && StringUtils.isNotBlank(url)) {
            String sourceAddress = "http://" + minioProperties.getEndpoint() + ":" + minioProperties.getPort() + "/" + minioProperties.getBucketName();
            String targetAddress = nginxProperties.getPrefixNginxUrl() + minioProperties.getNginxLoadUrl();
            url = url.replace(sourceAddress, targetAddress);
        }
        return url;
    }

    /**
     * 生成一个给HTTP PUT请求用的presigned URL。
     * 浏览器/移动端的客户端可以用这个URL进行上传，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return string
     */
    @SneakyThrows
    public String preSignedPutObject(String bucketName, String objectName, Integer expires) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(Objects.isNull(expires) ? DEFAULT_EXPIRY_TIME : expires)
                    .build()
            );
        }
        return url;
    }

    /**
     * 获取对象的元数据
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return stat object response
     */
    @SneakyThrows
    public StatObjectResponse statObject(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        }
        return null;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return object url
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .object(objectName)
                    .bucket(bucketName)
                    .build()
            );
        }
        return url;
    }


    /**
     * 文件下载
     *
     * @param bucketName   桶名称
     * @param objectName   桶中文件名
     * @param originalName 下载文件的名称
     * @param request      请求
     * @param response     请求响应
     */
    public void downloadFile(String bucketName,
                             String objectName,
                             String originalName,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            InputStream file = getObject(bucketName, objectName);
            String fileName = StrUtil.isNotEmpty(originalName) ? originalName : objectName;
            fileName = fileName.replace(" ", "");
            //文件名乱码处理
            String useragent = request.getHeader("USER-AGENT").toLowerCase();
            if (useragent.contains("msie") || useragent.contains("like gecko") || useragent.contains("trident")) {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName());
            } else {
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
        } catch (Exception e) {
            log.error(String.format("下载文件:%s异常", objectName), e);
        }
    }

}
