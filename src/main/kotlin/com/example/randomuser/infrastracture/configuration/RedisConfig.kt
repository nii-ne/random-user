package com.example.randomuser.infrastracture.configuration

import com.example.randomuser.application.response.UserResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(private val redisProperties: RedisProperties) {
    @Bean(name = ["jedisConnectionFactory"])
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val jedisConnectionFactory =  JedisConnectionFactory()
        jedisConnectionFactory.hostName = redisProperties.host
        jedisConnectionFactory.port = redisProperties.port
        jedisConnectionFactory.usePool = true
        return jedisConnectionFactory
    }

    @Bean(name = ["mainRedisTemplate"])
    fun redisTemplate(jedisConnectionFactory: JedisConnectionFactory): RedisTemplate<String, UserResponse> {
        val redisTemplate = RedisTemplate<String, UserResponse>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.setConnectionFactory(jedisConnectionFactory)
        return redisTemplate
    }
}