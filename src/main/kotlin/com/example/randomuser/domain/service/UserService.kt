package com.example.randomuser.domain.service

import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.domain.client.RandomClient
import com.example.randomuser.infrastracture.constants.ResponseCode
import com.example.randomuser.infrastracture.exception.CommonException
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserService(private val randomClient: RandomClient) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)
//    @Cacheable(value = ["user"], key = "#seed", unless = "#seed == null")
    fun getRandomUser(seed: String? = null): UserResponse {
        val response = randomClient.getRandomUser(seed) ?: throw CommonException(ResponseCode.NO_CONTENT)
        return UserResponse(response).also {logger.info("UserService.getRandomUser: {}", it)}
    }

}