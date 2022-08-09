package com.example.randomuser.infrastracture.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "rest-template.random-user")
class RandomUserProperties {
    lateinit var maxTotal: Number
    lateinit var defaultMaxPerRoute: Number
    lateinit var connectTimeout: Number
    lateinit var socketTimeout: Number
    lateinit var keepAliveHeader: Number
    lateinit var endpoints: Map<String, String>
}