package com.example.randomuser.domain.service

import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.domain.client.RandomClient
import com.example.randomuser.infrastracture.constants.ResponseCode
import com.example.randomuser.infrastracture.exception.CommonException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserService(private val randomClient: RandomClient) {
    @Cacheable(value = ["user"], key = "#seed", unless = "#seed == null")
    fun getRandomUser(seed: String?): UserResponse {
        val response = randomClient.getRandomUser(seed) ?: throw CommonException(ResponseCode.NO_CONTENT)
        return UserResponse(response)
    }

}