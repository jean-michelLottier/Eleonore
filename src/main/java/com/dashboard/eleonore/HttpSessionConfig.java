package com.dashboard.eleonore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
@EnableRedisHttpSession
@PropertySource("classpath:application.properties")
public class HttpSessionConfig extends AbstractHttpSessionApplicationInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionConfig.class);

    @Value("${spring.redis.host}")
    private String springRedisHost;

    @Value("${spring.redis.port}")
    private String springRedisPort;

    /**
     * Configuration of Redis using a Jedis client
     *
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        LOGGER.info("Configuration of Jedis client");
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(this.springRedisHost);
        configuration.setPort(Integer.parseInt(this.springRedisPort));

        return new JedisConnectionFactory(configuration);
    }

    /**
     * This can be used for querying data with a custom repository
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());

        return template;
    }

    /**
     * Customize Spring Session’s HttpSession integration to use HTTP headers
     * to convey the current session information instead of cookies
     *
     * @return
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
