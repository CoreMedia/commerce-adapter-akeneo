package com.coremedia.commerce.adapter.akeneo.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class AkeneoApiCacheConfiguration {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCache channelCache = buildCache("channels", 100,24, TimeUnit.HOURS);
    CaffeineCache categoryCache = buildCache("categories", 100,30, TimeUnit.MINUTES);
    CaffeineCache childCategoriesCache = buildCache("childCategories", 100, 30, TimeUnit.MINUTES);
    CaffeineCache productCache = buildCache("products", 1000, 24, TimeUnit.HOURS);
    CaffeineCache productsInCategoryCache = buildCache("productsInCategory", 1000, 1, TimeUnit.HOURS);

    SimpleCacheManager manager = new SimpleCacheManager();
    manager.setCaches(Arrays.asList(channelCache, categoryCache, childCategoriesCache, productCache, productsInCategoryCache));

    return manager;
  }

  private CaffeineCache buildCache(String name, int capacity, long duration, TimeUnit timeUnit) {
    return new CaffeineCache(name, Caffeine.newBuilder()
            .expireAfterWrite(duration, timeUnit)
            .maximumSize(capacity)
            .build());
  }

}
