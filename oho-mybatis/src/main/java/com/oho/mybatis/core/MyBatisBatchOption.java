package com.oho.mybatis.core;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MyBatis 通用批量操作，未做多线程处理
 * 无锁处理，不保证数据一致性
 *
 * @param <T> the type parameter
 * @author MENGJIAO
 */
@Slf4j
@Component
public class MyBatisBatchOption<T> {

    /**
     * 批量插入，单线程循环插入
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    public void batchInsert(IService<T> service, List<T> records, int batchSize) {
        if (records == null || records.size() == 0) {
            return;
        }
        int size = records.size();
        int batch = size / batchSize;
        int mod = size % batchSize;
        for (int i = 0; i < batch; i++) {
            service.saveBatch(records.subList(i * batchSize, (i + 1) * batchSize));
        }
        if (mod != 0) {
            service.saveBatch(records.subList(batch * batchSize, batch * batchSize + mod));
        }
    }

    /**
     * 异步批量插入，单线程循环插入
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    @Async
    public void asyncBatchInsert(IService<T> service, List<T> records, int batchSize) {
        batchInsert(service, records, batchSize);
    }

    /**
     * 批量删除
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    public void batchDelete(IService<T> service, List<T> records, int batchSize) {
        if (records == null || records.size() == 0) {
            return;
        }
        int size = records.size();
        int batch = size / batchSize;
        int mod = size % batchSize;
        for (int i = 0; i < batch; i++) {
            service.removeByIds(records.subList(i * batchSize, (i + 1) * batchSize));
        }
        if (mod != 0) {
            service.removeByIds(records.subList(batch * batchSize, batch * batchSize + mod));
        }
    }

    /**
     * 异步批量删除
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    @Async
    public void asyncBatchDelete(IService<T> service, List<T> records, int batchSize) {
        batchDelete(service, records, batchSize);
    }

    /**
     * 批量更新
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    public void batchUpdate(IService<T> service, List<T> records, int batchSize) {
        if (records == null || records.size() == 0) {
            return;
        }
        int size = records.size();
        int batch = size / batchSize;
        int mod = size % batchSize;
        for (int i = 0; i < batch; i++) {
            service.saveOrUpdateBatch(records.subList(i * batchSize, (i + 1) * batchSize));
        }
        if (mod != 0) {
            service.saveOrUpdateBatch(records.subList(batch * batchSize, batch * batchSize + mod));
        }
    }

    /**
     * 异步批量更新
     *
     * @param service   操作类
     * @param records   待操作数据
     * @param batchSize 单次操作数量
     */
    @Async
    public void asyncBatchUpdate(IService<T> service, List<T> records, int batchSize) {
        batchUpdate(service, records, batchSize);
    }
}
