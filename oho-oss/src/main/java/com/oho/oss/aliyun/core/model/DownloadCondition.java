package com.oho.oss.aliyun.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 限定条件
 * <p>
 * OSS支持的限定条件如下：
 * 说明
 * If-Modified-Since和If-Unmodified-Since可以同时存在。If-Match和If-None-Match也可以同时存在。
 * 您可以通过ossClient.getObjectMeta方法获取ETag。
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 11:37
 */
@Data
public class DownloadCondition {
    /**
     * 如果指定的ETag和OSS文件的ETag匹配，则正常传输文件，否则返回错误（412 Precondition failed）。
     */
    private List<String> matchingETagConstraints = new ArrayList<String>();
    /**
     * 如果指定的ETag和OSS文件的ETag不匹配，则正常传输文件，否则返回错误（304 Not modified）。
     */
    private List<String> nonmatchingEtagConstraints = new ArrayList<String>();
    /**
     * 如果指定的时间等于或者晚于文件实际修改时间，则正常传输文件，否则返回错误（412 Precondition failed）。
     */
    private Date unmodifiedSinceConstraint;
    /**
     * 如果指定的时间早于实际修改时间，则正常传输文件，否则返回错误（304 Not modified）。
     */
    private Date modifiedSinceConstraint;
}
