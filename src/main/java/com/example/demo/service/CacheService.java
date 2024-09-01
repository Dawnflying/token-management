package com.example.demo.service;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // 清除指定缓存名称下的多个键
    public void evictMultipleKeys(String cacheName, String... keys) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            for (String key : keys) {
                cache.evict(key);
            }
        }
    }
}
