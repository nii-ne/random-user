package com.example.randomuser.infrastracture.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "rest-template.random-user")
data class RandomUserProperties(
    val maxTotal: Number,
    val defaultMaxPerRoute: Number,
    val connectTimeout: Number,
    val socketTimeout: Number,
    val keepAliveHeader: Number,
    val endpoints: Map<String, String>
)