package com.example.randomuser.infrastracture.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "redis")
class RedisProperties {
    lateinit var ttl: Number
}