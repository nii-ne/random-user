package com.example.randomuser.infrastracture.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.redis")
data class RedisProperties (
    val host: String,
    val port: Int,
    val ttl: Number
)