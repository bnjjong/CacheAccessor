package io.jjong.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@TestInstance(Lifecycle.PER_CLASS)
@Extensions({@ExtendWith({MockitoExtension.class})})
class CacheAccessorTest {
  @Mock
  private LocalCacheManager localCacheManager;

  @Mock
  private RemoteCacheManager remoteCacheManager;

  private CacheAccessor cacheAccessor;

  @BeforeAll
  void init() {
    cacheAccessor = new CacheAccessor(localCacheManager, remoteCacheManager);
  }

  @Nested
  class Retrieve {

    @Test
    void success() {

    }
  }


}