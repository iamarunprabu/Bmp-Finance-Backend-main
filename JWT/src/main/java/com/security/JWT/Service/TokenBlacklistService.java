package com.security.JWT.Service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private LoadingCache<String, Date> blacklistedTokenCache;

    public TokenBlacklistService() {
        super();
        // Cache expires tokens after 5 days (matches JWT expiration time)
        blacklistedTokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.DAYS)
                .maximumSize(1000)
                .build(new CacheLoader<String, Date>() {
                    @Override
                    public Date load(String key) {
                        return new Date();
                    }
                });
    }

    /**
     * Add a token to the blacklist
     *
     * @param token The JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistedTokenCache.put(token, new Date());
    }

    /**
     * Check if a token is blacklisted
     *
     * @param token The JWT token to check
     * @return true if token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            return blacklistedTokenCache.getIfPresent(token) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remove a token from the blacklist (if needed for manual cleanup)
     *
     * @param token The JWT token to remove from blacklist
     */
    public void removeFromBlacklist(String token) {
        blacklistedTokenCache.invalidate(token);
    }

    /**
     * Get the number of blacklisted tokens currently in cache
     *
     * @return The size of the blacklist cache
     */
    public long getBlacklistSize() {
        return blacklistedTokenCache.size();
    }
}
