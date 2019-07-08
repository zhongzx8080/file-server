
package com.xing.fileserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;


@EnableCaching      // 启用缓存功能, 默认不启用
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

  private final RedisConnectionFactory redisConnectionFactory;

  @Autowired
  public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
    this.redisConnectionFactory = redisConnectionFactory;
  }

  /**
   * <h2>配置 CacheManager: 序列化方式、过期时间</h2>
   */
  @Override
  public CacheManager cacheManager() {

    // 初始化一个 RedisCacheWriter
    // RedisCacheWriter 提供了对 Redis 的 set、setnx、get 等命令的访问权限
    // 可以由多个缓存实现共享,并负责写/读来自 Redis 的二进制数据
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

    // 设置 CacheManager 的值序列化方式
    RedisSerializer<Object> jsonSerializer = new JdkSerializationRedisSerializer();
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
            .fromSerializer(jsonSerializer);
    // 提供 Redis 的配置
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(pair);

    // 设置默认超过期时间是 30 秒
    defaultCacheConfig.entryTtl(Duration.ofSeconds(30));

    // 初始化 RedisCacheManager 返回
    return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
  }

  /**
   * <h2>定义 key 生成器: 类名、方法名、参数列表</h2>
   * 自定义 KeyGenerator 的核心思想是保证 key 不会冲突
   * 默认的是 SimpleKeyGenerator, 它使用方法参数组合生成的一个 key, 这里存在一个问题:
   * 如果2个方法, 参数是一样的. 但执行逻辑不同, 那么将会导致执行第二个方法时命中第一个方法的缓存. 所以, 通常需要自定义.
   */
  @Override
  public KeyGenerator keyGenerator() {

    return (clazz, method, args) -> {

      StringBuilder sb = new StringBuilder();
      sb.append(clazz.getClass().getName()).append("#");
      sb.append(method.getName()).append("(");
      for (Object obj : args) {
        sb.append(obj.toString()).append(",");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append(")");
      return sb.toString();
    };
  }
}