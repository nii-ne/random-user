package com.example.randomuser

import com.example.randomuser.infrastracture.configuration.RandomUserProperties
import com.example.randomuser.infrastracture.configuration.RedisProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableCaching
@EnableConfigurationProperties(value = [RandomUserProperties::class, RedisProperties::class])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

