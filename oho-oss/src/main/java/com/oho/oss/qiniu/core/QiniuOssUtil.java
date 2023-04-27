package com.oho.oss.qiniu.core;

import com.oho.common.utils.JsonUtils;
import com.oho.oss.qiniu.autoconfigure.properties.QiniuOssProperties;
import com.oho.oss.qiniu.core.enums.GranularityEnum;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 七牛云对象存储工具类
 *
 * @author MENGJIAO
 * @createDate 2023 -04-27 13:20
 */
@Component
public class QiniuOssUtil {
    @Autowired
    private Auth auth;
    @Autowired
    private QiniuOssProperties qiniuOssProperties;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private Configuration configuration;

    /**
     * 获取上传Token
     *
     * @param bucketName the bucket name
     * @return the upload token
     */
    public String getUploadToken(String bucketName) {
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取覆盖Token
     *
     * @param bucketName the bucket name
     * @param key        the key
     * @return the cover token
     */
    public String getCoverToken(String bucketName, String key) {
        return auth.uploadToken(bucketName, key);
    }

    /**
     * Gets auth.
     *
     * @return the auth
     */
    public Auth getAuth() {
        return Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
    }

    // ==================== Bucket（存储空间）操作 ====================

    // ==================== 文件上传 ====================

    /**
     * 上传文件（适用于上传本地文件）
     *
     * @param bucketName    空间名
     * @param key           文件名
     * @param localFilePath 本地文件路径
     * @return the default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet uploadFile(String bucketName, String key, String localFilePath) throws QiniuException {
        // 获取上传的Token
        String upToken = getUploadToken(bucketName);
        // 执行文件上传
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    /**
     * 字节流上传
     *
     * @param bucketName  the bucket name
     * @param key         the key
     * @param uploadBytes the upload bytes
     * @return the default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet uploadFile(String bucketName, String key, byte[] uploadBytes) throws QiniuException {
        // 获取上传的Token
        String upToken = getUploadToken(bucketName);
        // 执行文件上传
        Response response = uploadManager.put(uploadBytes, key, upToken);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    /**
     * 数据流上传
     *
     * @param bucketName   the bucket name
     * @param key          the key
     * @param uploadStream the upload stream
     * @param params       the params
     * @param mime         the mime
     * @return the default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet uploadFile(String bucketName, String key, InputStream uploadStream, StringMap params, String mime) throws QiniuException {
        // 获取上传的Token
        String upToken = getUploadToken(bucketName);
        // 执行文件上传
        Response response = uploadManager.put(uploadStream, key, upToken, params, mime);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    /**
     * 文件覆盖
     *
     * @param bucketName    the bucket name
     * @param key           the key
     * @param localFilePath the local file path
     * @return default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet coverFile(String bucketName, String key, String localFilePath) throws QiniuException {
        // 获取上传的Token
        String upToken = getCoverToken(bucketName, key);
        // 执行文件上传
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    /**
     * 文件覆盖
     *
     * @param bucketName  the bucket name
     * @param key         the key
     * @param uploadBytes the upload bytes
     * @return the default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet coverFile(String bucketName, String key, byte[] uploadBytes) throws QiniuException {
        // 获取上传的Token
        String upToken = getCoverToken(bucketName, key);
        // 执行文件上传
        Response response = uploadManager.put(uploadBytes, key, upToken);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    /**
     * 文件覆盖
     *
     * @param bucketName   the bucket name
     * @param key          the key
     * @param uploadStream the upload stream
     * @param params       the params
     * @param mime         the mime
     * @return the default put ret
     * @throws QiniuException the qiniu exception
     */
    public DefaultPutRet coverFile(String bucketName, String key, InputStream uploadStream, StringMap params, String mime) throws QiniuException {
        // 获取上传的Token
        String upToken = getCoverToken(bucketName, key);
        // 执行文件上传
        Response response = uploadManager.put(uploadStream, key, upToken, params, mime);
        //解析上传成功的结果
        return JsonUtils.toObj(response.bodyString(), DefaultPutRet.class);
    }

    // ==================== 文件下载 ====================

    /**
     * 公开空间文件下载链接
     *
     * @param key the key
     * @return the public file url
     * @throws IOException the io exception
     */
    public String getPublicFileUrl(String key) throws IOException {
        DownloadUrl url = new DownloadUrl(qiniuOssProperties.getDomain(), qiniuOssProperties.isUseHttps(), key);
        return url.buildURL();
    }

    /**
     * 私有空间文件下载链接
     *
     * @param key             the key
     * @param expireInSeconds the expiry in seconds
     * @return the private file url
     * @throws IOException the io exception
     */
    public String getPrivateFileUrl(String key, long expireInSeconds) throws IOException {
        DownloadUrl url = new DownloadUrl(qiniuOssProperties.getDomain(), qiniuOssProperties.isUseHttps(), key);
        // 带有效期
        return url.buildURL(getAuth(), expireInSeconds);
    }

    // ==================== 资源管理 ====================

    /**
     * 获取文件信息
     *
     * @param bucketName the bucket name
     * @param key        the key
     * @return the file info
     * @throws QiniuException the qiniu exception
     */
    public FileInfo getFileInfo(String bucketName, String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        return bucketManager.stat(bucketName, key);
    }

    /**
     * 修改文件类型
     *
     * @param bucketName  the bucket name
     * @param key         the key
     * @param newMimeType the new mime type
     * @throws QiniuException the qiniu exception
     */
    public void changeFileType(String bucketName, String key, String newMimeType) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        bucketManager.changeMime(bucketName, key, newMimeType);
    }

    /**
     * 移动或重命名文件
     *
     * @param bucketName   the bucket name
     * @param key          the key
     * @param toBucketName the to bucket name
     * @param toKey        the to key
     * @throws QiniuException the qiniu exception
     */
    public void moveFile(String bucketName, String key, String toBucketName, String toKey) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        bucketManager.move(bucketName, key, toBucketName, toKey);
    }

    /**
     * 复制文件副本
     *
     * @param bucketName   the bucket name
     * @param key          the key
     * @param toBucketName the to bucket name
     * @param toKey        the to key
     * @throws QiniuException the qiniu exception
     */
    public void copyFile(String bucketName, String key, String toBucketName, String toKey) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        bucketManager.copy(bucketName, key, toBucketName, toKey);
    }

    /**
     * 删除空间中的文件
     *
     * @param bucketName the bucket name
     * @param key        the key
     * @throws QiniuException the qiniu exception
     */
    public void deleteFile(String bucketName, String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        bucketManager.delete(bucketName, key);
    }

    /**
     * 设置或更新文件的生存时间
     * </p>
     * 生存时间过后，文件将会被自动删除
     *
     * @param bucketName the bucket name
     * @param key        the key
     * @param days       the days
     * @throws QiniuException the qiniu exception
     */
    public void deleteAfterDays(String bucketName, String key, int days) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        bucketManager.deleteAfterDays(bucketName, key, days);
    }

    /**
     * 获取空间文件列表
     *
     * @param bucketName the bucket name
     * @param prefix     文件名前缀
     * @param limit      每次迭代的长度限制，最大1000，推荐值 1000
     * @param delimiter  指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @return the file list
     */
    public List<FileInfo> getFileList(String bucketName, String prefix, int limit, String delimiter) {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);

        List<FileInfo> fileInfoList = new ArrayList<>();
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            fileInfoList.addAll(Arrays.asList(items));
        }
        return fileInfoList;
    }

    /**
     * 抓取网络资源到空间
     *
     * @param bucketName   the bucket name
     * @param remoteSrcUrl the remote src url
     * @param key          the key
     * @return the fetch ret
     * @throws QiniuException the qiniu exception
     */
    public FetchRet fetch(String bucketName, String remoteSrcUrl, String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        //抓取网络资源到空间
        return bucketManager.fetch(remoteSrcUrl, bucketName, key);
    }

    // ==================== 资源管理批量操作 ====================

    /**
     * 批量获取文件信息
     * <p>
     * 单次批量请求的文件数量不得超过1000
     *
     * @param bucketName the bucket name
     * @param keys       the keys
     * @return the list
     * @throws QiniuException the qiniu exception
     */
    public List<BatchStatus> batchGetFileInfo(String bucketName, String[] keys) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addStatOps(bucketName, keys);
        Response response = bucketManager.batch(batchOperations);
        return Arrays.asList(response.jsonToObject(BatchStatus[].class));
    }

    /**
     * 批量修改文件类型
     *
     * @param bucketName the bucket name
     * @param keyMimeMap the key mime map
     * @return the list
     * @throws QiniuException the qiniu exception
     */
    public List<BatchStatus> batchChangeFileType(String bucketName, HashMap<String, String> keyMimeMap) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);

        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        //添加指令
        for (Map.Entry<String, String> entry : keyMimeMap.entrySet()) {
            String key = entry.getKey();
            String newMimeType = entry.getValue();
            batchOperations.addChgmOp(bucketName, key, newMimeType);
        }
        Response response = bucketManager.batch(batchOperations);
        return Arrays.asList(response.jsonToObject(BatchStatus[].class));
    }

    /**
     * 批量删除文件
     * <p>
     * 单次批量请求的文件数量不得超过1000
     *
     * @param bucketName the bucket name
     * @param keyList    the key list
     * @return the list
     * @throws QiniuException the qiniu exception
     */
    public List<BatchStatus> batchDeleteFile(String bucketName, String[] keyList) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        //单次批量请求的文件数量不得超过1000
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucketName, keyList);
        Response response = bucketManager.batch(batchOperations);
        return Arrays.asList(response.jsonToObject(BatchStatus[].class));
    }

    /**
     * 批量移动或重命名文件
     * <p>
     * 单次批量请求的文件数量不得超过1000
     *
     * @param bucketName   the bucket name
     * @param toBucketName the to bucket name
     * @param keyList      the key list
     * @return the list
     * @throws QiniuException the qiniu exception
     */
    public List<BatchStatus> batchMoveFile(String bucketName, String toBucketName, String[] keyList) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        // 添加指令
        for (String key : keyList) {
            batchOperations.addMoveOp(bucketName, key, toBucketName, key + "_move");
        }
        Response response = bucketManager.batch(batchOperations);
        return Arrays.asList(response.jsonToObject(BatchStatus[].class));
    }

    /**
     * 批量复制文件
     * <p>
     * 单次批量请求的文件数量不得超过1000
     *
     * @param bucketName   the bucket name
     * @param toBucketName the to bucket name
     * @param keyList      the key list
     * @return the list
     * @throws QiniuException the qiniu exception
     */
    public List<BatchStatus> batchCopyFile(String bucketName, String toBucketName, String[] keyList) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        // 添加指令
        for (String key : keyList) {
            batchOperations.addCopyOp(bucketName, key, toBucketName, key + "_copy");
        }
        Response response = bucketManager.batch(batchOperations);
        return Arrays.asList(response.jsonToObject(BatchStatus[].class));
    }

    /**
     * 更新镜像存储空间中文件内容
     * <p>
     * 单次批量请求的文件数量不得超过1000
     *
     * @param bucketName the bucket name
     * @param key        the key
     * @return the response
     * @throws QiniuException the qiniu exception
     */
    public Response batchPrefetchFile(String bucketName, String key) throws QiniuException {
        BucketManager bucketManager = new BucketManager(getAuth(), configuration);
        return bucketManager.prefetch(bucketName, key);
    }

    // ==================== CDN相关功能 ====================

    /**
     * 文件刷新
     *
     * @param urls the urls
     * @return the cdn result . refresh result
     * @throws QiniuException the qiniu exception
     */
    public CdnResult.RefreshResult refreshCdnUrls(String[] urls) throws QiniuException {
        CdnManager c = new CdnManager(getAuth());
        //待刷新的链接列表
        //单次方法调用刷新的链接不可以超过100个
        return c.refreshUrls(urls);
    }

    /**
     * 目录刷新
     *
     * @param dirs the dirs
     * @return the cdn result . refresh result
     * @throws QiniuException the qiniu exception
     */
    public CdnResult.RefreshResult refreshCdnDirs(String[] dirs) throws QiniuException {
        CdnManager c = new CdnManager(getAuth());
        //待刷新的目录列表
        //单次方法调用刷新的目录不可以超过10个
        return c.refreshDirs(dirs);
    }

    /**
     * 获取域名流量
     *
     * @param domains     the domains
     * @param startDate   the start date
     * @param endDate     the end date
     * @param granularity the granularity
     * @return the bandwidth data
     * @throws QiniuException the qiniu exception
     */
    public CdnResult.FluxResult getBandwidthData(String[] domains, String startDate, String endDate, GranularityEnum granularity) throws QiniuException {
        CdnManager c = new CdnManager(getAuth());
        return c.getFluxData(domains, startDate, endDate, granularity.getValue());
    }

    /**
     * 获取域名带宽
     *
     * @param domains     the domains
     * @param startDate   the start date
     * @param endDate     the end date
     * @param granularity the granularity
     * @return the flux data
     * @throws QiniuException the qiniu exception
     */
    public CdnResult.BandwidthResult getFluxData(String[] domains, String startDate, String endDate, GranularityEnum granularity) throws QiniuException {
        CdnManager c = new CdnManager(getAuth());
        return c.getBandwidthData(domains, startDate, endDate, granularity.getValue());
    }

    /**
     * 获取域名日志下载链接
     *
     * @param domains the domains
     * @param logDate the log date
     * @return the cdn log list
     * @throws QiniuException the qiniu exception
     */
    public CdnResult.LogListResult getCdnLogList(String[] domains, String logDate) throws QiniuException {
        CdnManager c = new CdnManager(getAuth());
        return c.getCdnLogList(domains, logDate);
    }

}
