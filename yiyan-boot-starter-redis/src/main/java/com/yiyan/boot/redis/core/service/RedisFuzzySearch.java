package com.yiyan.boot.redis.core.service;

import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis模糊搜索
 *
 * @author MENGJIAO
 * @createDate 2023-11-07 0007 上午 11:09
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisFuzzySearch {

    private final RedissonClient redissonClient;

    /**
     * 搜索
     *
     * @param key   缓存key
     * @param query 搜索关键字
     * @param limit 搜索结果数量
     * @return 搜索结果
     */
    public List<String> search(String key, String query, int limit) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(key);
        if (sortedSet.isEmpty()) {
            return Lists.newArrayList();
        }
        // 搜索关键字为空，获取权重最高
        if (StringUtil.isBlank(query)) {
            Collection<ScoredEntry<String>> entries = sortedSet.entryRange(0, limit - 1);
            return entries.stream().map(ScoredEntry::getValue).collect(Collectors.toList());
        }
        // 根据关键字搜索
        Iterator<String> iterator = sortedSet.iterator("*" + query + "*", limit);
        List<String> res = Lists.newArrayList();
        while (iterator.hasNext()) {
            String next = iterator.next();
            res.add(String.valueOf(next));
            // 搜索结果权重+1
            sortedSet.addScore(next, sortedSet.getScore(next) + 1);
        }
        return res;
    }
}
