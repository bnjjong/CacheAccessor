package io.jjong.cache;

import java.time.Duration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * create on 2022/07/05. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 *
 * @author Jongsang Han(henry)
 * @version 1.0
 * @see
 * @since 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheAccessor<T1> {
  private LocalCacheManager<T1> localCacheManager;
  private RemoteCacheManager<T1> remoteCacheManager;

  private static Cache emptyKeyCache;
  static {
    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .withCache("emptyCacheConfigured",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                ResourcePoolsBuilder.heap(50))
                .withExpiry(
                    ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10))
                )
                .withExpiry(
                    ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(5))
                )
        )
        .build();
    cacheManager.init();
    emptyKeyCache = cacheManager.getCache("emptyCacheConfigured", String.class, String.class);
  }


  public CacheAccessor(LocalCacheManager<T1> localCacheManager,
      RemoteCacheManager<T1> remoteCacheManager) {
//    if (localCacheManager == null && remoteCacheManager == null) {
//      throw new CacheAccessorConstructException("Can't create instance without cache manager.");
//    }

    this.localCacheManager = localCacheManager;
    this.remoteCacheManager = remoteCacheManager;
  }

  public <T2> T1 retrieve(@NonNull String key, @NonNull DBManager<T1> dbManager) {
    T1 value = null;
    SearchType searchType = SearchType.NONE;
    try {
      if (emptyKeyCache.containsKey(key)) {
        return null;
      }

      value = localCacheManager.get(key);
      if (value != null) {
        searchType = SearchType.LOCAL_CACHE;
        return value;
      }

      value = remoteCacheManager != null ? remoteCacheManager.get(key) : null;
      if (value != null) {
        searchType = SearchType.REMOTE_CACHE;
        return value;
      }

      value = dbManager.get();
      if (value != null) {
        searchType = SearchType.DB;
        return value;
      }

      emptyKeyCache.put(key, "");
      return null;
    } finally {
      restore(key, value, searchType);
      print(key, value, searchType);
    }
  }

  private void print(String key, T1 value, SearchType searchType) {
    if (log.isDebugEnabled()) {
      log.debug("db type:{}, key:{}, value:{}", searchType, key, value);
    }
  }

  private void restore(String key, T1 value, SearchType searchType) {
    if (searchType == SearchType.LOCAL_CACHE) {
      return;
    }
    if (searchType == SearchType.REMOTE_CACHE) {
      localCacheManager.put(key, value);
      return;
    }
    if (searchType == SearchType.DB) {
      localCacheManager.put(key,value);
      remoteCacheManager.put(key, value);
      return;
    }
  }

}
