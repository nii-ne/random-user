package com.example.randomuser.domain.service

import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.infrastracture.configuration.RedisProperties
import com.example.randomuser.infrastracture.constants.Namespace
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Service
class RedisService(private val userService: UserService, private val redisProperties: RedisProperties) {

    private val logger = LoggerFactory.getLogger(RedisService::class.java)

    @Resource(name = "mainRedisTemplate")
    private lateinit var valueOperations: ValueOperations<String, UserResponse>


    fun getUser(seed: String): UserResponse {
        val key = Namespace.RANDOM_USER_SERVICE.plus(":").plus(seed)
        return get(key)
            ?: userService.getRandomUser(seed).also {
                kotlin.runCatching {
                    put(key, it, redisProperties.ttl.toLong())
                }.onFailure { logger.error("save data to redis error") }
            }
    }
    private fun get(key: String) = kotlin.runCatching {
        valueOperations.get(key).also {
            logger.info("Getting user from cache with: {}, response: {}", key, it)
        }
    }.getOrNull()
    private fun put(key: String, data: UserResponse, ttl: Long) = valueOperations.set(key, data, ttl, TimeUnit.MILLISECONDS).also {
        logger.info("Caching user with: {}, value: {}", key, data) }


}
