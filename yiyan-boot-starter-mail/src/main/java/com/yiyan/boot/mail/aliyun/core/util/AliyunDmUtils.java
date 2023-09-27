package com.yiyan.boot.mail.aliyun.core.util;

import com.aliyun.dm20151123.models.*;
import com.oho.common.enums.YesNoEnum;
import com.oho.common.utils.JsonUtils;
import com.oho.common.utils.date.DateUtils;
import com.yiyan.boot.mail.aliyun.core.model.content.DmBatchSendMail;
import com.yiyan.boot.mail.aliyun.core.model.content.DmSingleSendMail;
import com.yiyan.boot.mail.aliyun.core.model.task.DmTaskQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 阿里云DM 邮件推送服务工具类
 *
 * @author MENGJIAO
 * @createDate 2023-05-04 16:46
 */
@Slf4j
@Component
public class AliyunDmUtils {

    @Autowired
    private com.aliyun.dm20151123.Client client;

    // ===========================  邮件推送服务  ===========================

    /**
     * 发送单条邮件
     */
    public SingleSendMailResponse sendSingleMail(@Validated DmSingleSendMail dmSingleSendMail) throws Exception {
        com.aliyun.dm20151123.models.SingleSendMailRequest singleSendMailRequest = new com.aliyun.dm20151123.models.SingleSendMailRequest()
                // 发信地址
                .setAccountName(dmSingleSendMail.getAccountName())
                // 地址类型
                .setAddressType(dmSingleSendMail.getAddressType().getValue())
                // 标签
                .setTagName(dmSingleSendMail.getTagName())
                // 目标地址
                .setToAddress(dmSingleSendMail.getToAddress())
                // 邮件主题
                .setSubject(dmSingleSendMail.getSubject())
                // 邮件HTML正文
                .setHtmlBody(dmSingleSendMail.getHtmlBody())
                // 邮件Text正文
                .setTextBody(dmSingleSendMail.getTextBody())
                // 发信人昵称
                .setFromAlias(dmSingleSendMail.getFromAlias())
                // 回信地址
                .setReplyAddress(dmSingleSendMail.getReplyAddress())
                // 回信人昵称
                .setReplyAddressAlias(dmSingleSendMail.getReplyAddressAlias())
                // 打开数据跟踪功能
                .setClickTrace(dmSingleSendMail.getClickTrace());
        SingleSendMailResponse singleSendMailResponse = client.singleSendMail(singleSendMailRequest);
        log.info("邮件服务 - AliyunDM - 发送单条邮件 - 记录时间 : [{}], 结果：[{}]", DateUtils.now(), JsonUtils.toJson(singleSendMailResponse));
        return singleSendMailResponse;
    }

    /**
     * 批量发送邮件
     */
    public com.aliyun.dm20151123.models.BatchSendMailResponse batchSendMail(@Validated DmBatchSendMail dmBatchSendMail) throws Exception {
        com.aliyun.dm20151123.models.BatchSendMailRequest batchSendMailRequest = new com.aliyun.dm20151123.models.BatchSendMailRequest()
                //预先创建且通过审核的模板名称
                .setTemplateName(dmBatchSendMail.getTemplateName())
                // 发信地址
                .setAccountName(dmBatchSendMail.getAccountName())
                // 预先创建且上传了收件人的收件人列表名称
                .setReceiversName(dmBatchSendMail.getReceiversName())
                // 地址类型
                .setAddressType(dmBatchSendMail.getAddressType().getValue())
                // 标签
                .setTagName(dmBatchSendMail.getTagName())
                // 回信地址
                .setReplyAddress(dmBatchSendMail.getReplyAddress())
                // 回信人昵称
                .setReplyAddressAlias(dmBatchSendMail.getReplyAddressAlias())
                // 打开数据跟踪功能
                .setClickTrace(dmBatchSendMail.getClickTrace());
        BatchSendMailResponse batchSendMailResponse = client.batchSendMail(batchSendMailRequest);
        log.info("邮件服务 - AliyunDM - 批量发送邮件 - 记录时间 : [{}], 结果：[{}]", DateUtils.now(), JsonUtils.toJson(batchSendMailResponse));
        return batchSendMailResponse;
    }

    // ===========================  服务参数  ===========================

    /**
     * 获取账户信息
     */
    public DescAccountSummaryResponse queryMailAddressByParam() throws Exception {
        com.aliyun.dm20151123.models.DescAccountSummaryRequest descAccountSummaryRequest = new com.aliyun.dm20151123.models.DescAccountSummaryRequest();
        return client.descAccountSummary(descAccountSummaryRequest);
    }

    // ===========================  白名单  ===========================

    /**
     * 添加IP白名单
     * </p>
     * IP地址/IP区间/IP段
     */
    public com.aliyun.dm20151123.models.AddIpfilterResponse addIpfilter(String ip) throws Exception {
        com.aliyun.dm20151123.models.AddIpfilterRequest addIpfilterRequest = new com.aliyun.dm20151123.models.AddIpfilterRequest()
                .setIpAddress(ip);
        return client.addIpfilter(addIpfilterRequest);
    }

    /**
     * 删除IP白名单
     * </p>
     * 记录id
     */
    public DeleteIpfilterByEdmIdResponse deleteIpfilter(String id) throws Exception {
        com.aliyun.dm20151123.models.DeleteIpfilterByEdmIdRequest deleteIpfilterByEdmIdRequest = new com.aliyun.dm20151123.models.DeleteIpfilterByEdmIdRequest()
                .setId(id);
        return client.deleteIpfilterByEdmId(deleteIpfilterByEdmIdRequest);
    }

    // ===========================  IP 保护  ===========================

    /**
     * 开启或关闭IP保护
     */
    public UpdateIpProtectionResponse setIpProtection(YesNoEnum choose) throws Exception {
        com.aliyun.dm20151123.models.UpdateIpProtectionRequest updateIpProtectionRequest = new com.aliyun.dm20151123.models.UpdateIpProtectionRequest()
                .setIpProtection(choose.getKey().toString());
        return client.updateIpProtection(updateIpProtectionRequest);
    }

    /**
     * 查询IP保护状态
     */
    public GetIpProtectionResponse queryIpProtection() throws Exception {
        com.aliyun.dm20151123.models.GetIpProtectionRequest getIpProtectionRequest = new com.aliyun.dm20151123.models.GetIpProtectionRequest();
        return client.getIpProtection(getIpProtectionRequest);
    }

    /**
     * 获取IP筛选器列表
     */
    public GetIpfilterListResponse queryIpfilterList() throws Exception {
        com.aliyun.dm20151123.models.GetIpfilterListRequest getIpfilterListRequest = new com.aliyun.dm20151123.models.GetIpfilterListRequest();
        return client.getIpfilterList(getIpfilterListRequest);
    }

    // ===========================  任务  ===========================

    /**
     * 查询任务列表
     */
    public QueryTaskByParamResponse queryTaskList(DmTaskQueryDTO dmTaskQueryDTO) throws Exception {
        com.aliyun.dm20151123.models.QueryTaskByParamRequest queryTaskByParamRequest = new com.aliyun.dm20151123.models.QueryTaskByParamRequest()
                .setKeyWord(dmTaskQueryDTO.getKeyWord())
                .setStatus(dmTaskQueryDTO.getStatus())
                .setPageNo(dmTaskQueryDTO.getPageNo())
                .setPageSize(dmTaskQueryDTO.getPageSize());
        return client.queryTaskByParam(queryTaskByParamRequest);
    }

    // ===========================  域名  ===========================

    /**
     * 创建域名
     */
    public CreateDomainResponse createDomain(String domainName) throws Exception {
        com.aliyun.dm20151123.models.CreateDomainRequest createDomainRequest = new com.aliyun.dm20151123.models.CreateDomainRequest()
                .setDomainName(domainName);
        return client.createDomain(createDomainRequest);
    }

    /**
     * 删除域名
     */
    public DeleteDomainResponse deleteDomain(Integer domainId) throws Exception {
        com.aliyun.dm20151123.models.DeleteDomainRequest deleteDomainRequest = new com.aliyun.dm20151123.models.DeleteDomainRequest()
                .setDomainId(domainId);
        return client.deleteDomain(deleteDomainRequest);
    }

    /**
     * 设置域名的SMTP的密码
     */
    public ModifyPWByDomainResponse setDomainPassword(String domainName, String password) throws Exception {
        com.aliyun.dm20151123.models.ModifyPWByDomainRequest modifyPWByDomainRequest = new com.aliyun.dm20151123.models.ModifyPWByDomainRequest()
                .setDomainName(domainName)
                .setPassword(password);
        return client.modifyPWByDomain(modifyPWByDomainRequest);
    }

    /**
     * 查询域名列表信息
     */
    public QueryDomainByParamResponse queryDomainList(DmTaskQueryDTO dmTaskQueryDTO) throws Exception {
        com.aliyun.dm20151123.models.QueryDomainByParamRequest queryDomainByParamRequest = new com.aliyun.dm20151123.models.QueryDomainByParamRequest()
                .setKeyWord("")
                .setStatus(1)
                .setPageNo(1)
                .setPageSize(1);
        return client.queryDomainByParam(queryDomainByParamRequest);
    }

    /**
     * 验证域名
     */
    public CheckDomainResponse checkDomain(Integer domainId) throws Exception {
        com.aliyun.dm20151123.models.CheckDomainRequest checkDomainRequest = new com.aliyun.dm20151123.models.CheckDomainRequest()
                .setDomainId(1);
        return client.checkDomain(checkDomainRequest);
    }

    /**
     * 配置域名
     */
    public DescDomainResponse descDomain(Integer domainId) throws Exception {
        com.aliyun.dm20151123.models.DescDomainRequest descDomainRequest = new com.aliyun.dm20151123.models.DescDomainRequest()
                .setDomainId(domainId);
        return client.descDomain(descDomainRequest);
    }
}
