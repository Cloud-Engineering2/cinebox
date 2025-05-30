package cinebox.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import cinebox.domain.auth.entity.TokenRedis;

@Configuration
@EnableRedisRepositories(
		enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
		keyspaceNotificationsConfigParameter = "")
public class RedisRepositoryConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public RedisKeyValueTemplate redisKeyValueTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, TokenRedis> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		return new RedisKeyValueTemplate(new RedisKeyValueAdapter(redisTemplate), new RedisMappingContext());
	}

    @Bean
    @Primary
    public RedisTemplate<String, String> redisStringTemplate(RedisConnectionFactory redisConnectionFactory) {
    	RedisTemplate<String, String> template = new RedisTemplate<>();
    	template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
    }
}
