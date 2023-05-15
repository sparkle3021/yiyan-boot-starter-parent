package com.oho.oss.aliyun.core;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.oho.common.utils.BeanUtils;
import com.oho.common.utils.date.DateUtils;
import com.oho.oss.aliyun.core.model.DownloadCondition;
import com.oho.oss.aliyun.core.model.ListObjectCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MENGJIAO
 * @createDate 2023-04-27 9:29
 */
@Component
public class AliyunOssUtil {

    @Autowired
    private OSS ossClient;

    // ============================ Bucket（存储空间） ============================

    /**
     * 创建存储空间
     */
    public String createBucket(String bucketName) {
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
        }
        return bucketName;
    }

    /**
     * 列举存储空间
     */
    public List<Bucket> listBuckets() {
        return ossClient.listBuckets();
    }

    /**
     * 判断存储空间是否存在
     */
    public boolean doesBucketExist(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 获取存储空间的地域
     */
    public String getBucketLocation(String bucketName) {
        return ossClient.getBucketLocation(bucketName);
    }

    /**
     * 获取存储空间的信息
     */
    public BucketInfo getBucketInfo(String bucketName) {
        return ossClient.getBucketInfo(bucketName);
    }

    /**
     * 获取存储空间的存储容量
     */
    public BucketStat getBucketStat(String bucketName) {
        return ossClient.getBucketStat(bucketName);
    }

    /**
     * 配置资源组
     */
    public void setBucketResourceGroup(String bucketName, String rgId) {
        // 创建setBucketResourceGroupRequest对象。
        SetBucketResourceGroupRequest setBucketResourceGroupRequest = new SetBucketResourceGroupRequest(bucketName, rgId);
        // 配置Bucket所属资源组。
        ossClient.setBucketResourceGroup(setBucketResourceGroupRequest);
    }

    /**
     * 获取资源组
     */
    public String getBucketResourceGroup(String bucketName) {
        // 获取Bucket所属资源组。
        return ossClient.getBucketResourceGroup(bucketName).getResourceGroupId();
    }

    /**
     * 删除存储空间
     */
    public void deleteBucket(String bucketName) {
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 存储空间标签
     */
    public void setBucketTagging(String bucketName, Map<String, String> tags) {
        // 设置Bucket标签。
        SetBucketTaggingRequest request = new SetBucketTaggingRequest(bucketName);
        // 依次填写Bucket标签的键（例如owner）和值（例如John）。
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            request.setTag(entry.getKey(), entry.getValue());
        }
        ossClient.setBucketTagging(request);
    }

    // ============================ Object（文件） ============================

    // ============================ 上传 ============================

    /**
     * 简单上传 - 上传字符串
     */
    public PutObjectResult putObject(String bucketName, String objectName, String content) {
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
        // 设置该属性可以返回response。如果不设置，则返回的response为空。
        putObjectRequest.setProcess("true");
        // 上传字符串。
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 简单上传 - 上传Byte数组
     */
    public PutObjectResult putObject(String bucketName, String objectName, byte[] content) {
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content));
        // 设置该属性可以返回response。如果不设置，则返回的response为空。
        putObjectRequest.setProcess("true");
        // 上传字节数组。
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 简单上传 - 上传网络流
     */
    public PutObjectResult putObject(String bucketName, String objectName, URL url) throws IOException {
        InputStream inputStream = url.openStream();
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        // 设置该属性可以返回response。如果不设置，则返回的response为空。
        putObjectRequest.setProcess("true");
        // 上传网络流。
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 简单上传 - 上传文件流
     */
    public PutObjectResult putObject(String bucketName, String objectName, InputStream inputStream) {
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        // 设置该属性可以返回response。如果不设置，则返回的response为空。
        putObjectRequest.setProcess("true");
        // 上传文件流。
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 简单上传 - 文件上传
     */
    public PutObjectResult putObject(String bucketName, String objectName, File file) {
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file);
        // 设置该属性可以返回response。如果不设置，则返回的response为空。
        putObjectRequest.setProcess("true");
        // 上传文件。
        return ossClient.putObject(putObjectRequest);
    }

    /**
     * 追加上传
     */
    public AppendObjectResult appendObject(String bucketName, String objectName, String contentType, List<InputStream> contents) {
        ObjectMetadata meta = new ObjectMetadata();
        // 指定上传的内容类型。
        meta.setContentType(contentType);
        // 通过AppendObjectRequest设置多个参数。
        AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName, objectName, contents.get(0), meta);
        // 第一次追加。
        // 设置文件的追加位置。
        appendObjectRequest.setPosition(0L);
        AppendObjectResult appendObjectResult = ossClient.appendObject(appendObjectRequest);
        // 后续追加
        for (int i = 1; i < contents.size(); i++) {
            // nextPosition表示下一次请求中应当提供的Position，即文件当前的长度。
            appendObjectRequest.setPosition(appendObjectResult.getNextPosition());
            appendObjectRequest.setInputStream(contents.get(i));
            appendObjectResult = ossClient.appendObject(appendObjectRequest);
        }
        return appendObjectResult;
    }

    /**
     * 分片上传
     */
    public CompleteMultipartUploadResult multipartUpload(String bucketName, String objectName, final File file) throws IOException {
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
        // 初始化分片。
        InitiateMultipartUploadResult upResult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        String uploadId = upResult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
        final long partSize = 1024 * 1024L;   //1 MB。

        // 根据上传的数据大小计算分片数。以本地文件为例，说明如何通过File.length()获取上传数据的大小。
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(objectName);
            uploadPartRequest.setUploadId(uploadId);
            // 设置上传的分片流。
            // 以本地文件为例说明如何创建FIleInputstream，并通过InputStream.skip()方法跳过指定数据。
            InputStream inStream = Files.newInputStream(file.toPath());
            inStream.skip(startPos);
            uploadPartRequest.setInputStream(inStream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber(i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }
        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
        // 完成分片上传。
        return ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 断点续传上传
     */
    public void checkpointUpload(String bucketName, String objectName, String contentType, String uploadFile) throws Throwable {
        ObjectMetadata meta = new ObjectMetadata();
        // 指定上传的内容类型。
        meta.setContentType(contentType);
        // 文件上传时设置访问权限ACL。
        // meta.setObjectAcl(CannedAccessControlList.Private);

        // 通过UploadFileRequest设置多个参数。
        // 依次填写Bucket名称（例如examplebucket）以及Object完整路径（例如exampledir/exampleobject.txt），Object完整路径中不能包含Bucket名称。
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, objectName);

        // 通过UploadFileRequest设置单个参数。
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        uploadFileRequest.setUploadFile(uploadFile);
        // 指定上传并发线程数，默认值为1。
        uploadFileRequest.setTaskNum(5);
        // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
        uploadFileRequest.setPartSize(1024 * 1024);
        // 开启断点续传，默认关闭。
        uploadFileRequest.setEnableCheckpoint(true);
        // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中，如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。上传完成后，该文件会被删除。
        // 如果未设置该值，默认与待上传的本地文件同路径，名称为${uploadFile}.ucp。
        // uploadFileRequest.setCheckpointFile("yourCheckpointFile");
        // 文件的元数据。
        uploadFileRequest.setObjectMetadata(meta);
        // 设置上传回调，参数为Callback类型。
        //uploadFileRequest.setCallback("yourCallbackEvent");
        // 断点续传上传。
        ossClient.uploadFile(uploadFileRequest);
    }

    // ============================== 下载 ==============================

    /**
     * 流式下载
     */
    public InputStream download(String bucketName, String objectName) throws Throwable {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        InputStream objectContent = ossObject.getObjectContent();
        // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        ossObject.close();
        return objectContent;
    }

    /**
     * 下载到本地文件
     */
    public ObjectMetadata download(String bucketName, String objectName, String downloadFilePath) throws Throwable {
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        return ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(downloadFilePath));
    }

    /**
     * 范围下载
     */
    public InputStream rangeDownload(String bucketName, String objectName, String downloadFilePath) throws Throwable {
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectName);
        // 对于大小为1000 Bytes的文件，正常的字节范围为0~999。
        // 获取0~999字节范围内的数据，包括0和999，共1000个字节的数据。如果指定的范围无效（比如开始或结束位置的指定值为负数，或指定值大于文件大小），则下载整个文件。
        getObjectRequest.setRange(0, 999);
        // 范围下载。
        return ossClient.getObject(getObjectRequest).getObjectContent();
    }

    /**
     * 断点续传下载
     */
    public DownloadFileResult checkpointDownload(String bucketName, String objectName, String downloadFilePath) throws Throwable {
        // 请求10个任务并发下载。
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, objectName);
        // 指定Object下载到本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        downloadFileRequest.setDownloadFile(downloadFilePath);
        // 设置分片下载的并发数，默认值为1。
        downloadFileRequest.setTaskNum(10);
        // 设置分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
        downloadFileRequest.setPartSize(1024 * 1024);
        // 开启断点续传下载，默认关闭。
        downloadFileRequest.setEnableCheckpoint(true);
        // 下载文件。
        ossClient.downloadFile(downloadFileRequest);
        // 下载文件。
        return ossClient.downloadFile(downloadFileRequest);
    }

    /**
     * 限定条件下载
     */
    public void conditionalDownload(String bucketName, String objectName, DownloadCondition condition, String downloadFilePath) {
        GetObjectRequest request = new GetObjectRequest(bucketName, objectName);
        // 设置限定条件。
        BeanUtils.copyProperties(condition, request);
        // 下载OSS文件到本地文件。
        ossClient.getObject(request, new File(downloadFilePath));
    }

    // ============================== 文件管理 ==============================

    /**
     * 判断文件是否存在
     */
    public boolean exist(String bucketName, String objectName) {
        return ossClient.doesObjectExist(bucketName, objectName);
    }

    /**
     * 管理文件元信息
     */
    public ObjectMetadata objectMetadata(byte[] content, String contentType, Map<String, String> headers, String downloadFileName) {
        // 创建上传文件的元信息，可以通过文件元信息设置HTTP header。
        ObjectMetadata meta = new ObjectMetadata();
        String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(content));
        // 开启文件内容MD5校验。开启后OSS会把您提供的MD5与文件的MD5比较，不一致则抛出异常。
        meta.setContentMD5(md5);
        // 指定上传的内容类型。内容类型决定浏览器将以什么形式、什么编码读取文件。如果没有指定则根据文件的扩展名生成，如果没有扩展名则为默认值application/octet-stream。
        meta.setContentType(contentType);
        // 设置内容被下载时的名称。
        meta.setContentDisposition("attachment; filename=" + downloadFileName);
        // 设置上传文件的长度。如超过此长度，则上传文件会被截断，上传的文件长度为设置的长度。如小于此长度，则为上传文件的实际长度。
        meta.setContentLength(content.length);
        // 设置内容被下载时网页的缓存行为。
        meta.setCacheControl("Download Action");
        // 设置缓存过期时间，格式是格林威治时间（GMT）。
        meta.setExpirationTime(DateUtils.toDate(LocalDateTime.now().plusHours(2)));
        // 设置内容被下载时的编码格式。
        meta.setContentEncoding("gzip");
        // 设置Header。
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            meta.setHeader(entry.getKey(), entry.getValue());
        }
        return meta;
    }

    /**
     * 转换文件存储类型
     */
    public CopyObjectResult changeStorageClass(String bucketName, String objectName, StorageClass storageClass) {
        // 创建CopyObjectRequest对象。
        CopyObjectRequest request = new CopyObjectRequest(bucketName, objectName, bucketName, objectName);
        // 创建ObjectMetadata对象。
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 封装header，此处以设置存储类型为归档类型为例。
        objectMetadata.setHeader("x-oss-storage-class", storageClass);
        request.setNewObjectMetadata(objectMetadata);
        // 更改文件存储类型。
        return ossClient.copyObject(request);
    }

    /**
     * 列举文件
     */
    public ObjectListing listObjects(ListObjectCondition condition) {
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(condition.getBucketName());
        // 设置参数。
        listObjectsRequest.setPrefix(condition.getPrefix());
        listObjectsRequest.setDelimiter(condition.getDelimiter());
        listObjectsRequest.setMarker(condition.getMarker());
        listObjectsRequest.setMaxKeys(condition.getMaxKeys());
        // 列举文件。
        return ossClient.listObjects(listObjectsRequest);
    }

    /**
     * 重命名文件
     */
    public void renameObject(String bucketName, String oldObjectName, String newObjectName) {
        // 重命名文件。
        ossClient.copyObject(bucketName, oldObjectName, bucketName, newObjectName);
        // 删除原文件。
        ossClient.deleteObject(bucketName, oldObjectName);
    }

    /**
     * 删除文件
     * 删除文件或目录。如果要删除目录，目录必须为空。
     */
    public void deleteObject(String bucketName, String objectName) {
        ossClient.deleteObject(bucketName, objectName);
    }
}
