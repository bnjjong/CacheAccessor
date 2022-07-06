package io.jjong.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;

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
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class LocalCacheManager<T1> {
  private Cache<String, T1> cache;

  /**
   *
   * @param key
   * @return
   */
  public T1 get(String key) {
    return cache.get(key);
  }

  public void put(String key, T1 value) {
    if (cache.containsKey(key) || validate(value)) {
      cache.put(key, value);
    }
  }

  /**
   *
   * @param value
   * @return
   */
  protected abstract boolean validate(T1 value);
}
