package com.example.randomuser.application.rest

import com.example.randomuser.application.response.CommonResponse
import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.domain.service.RedisService
import com.example.randomuser.domain.service.UserService
import com.example.randomuser.infrastracture.constants.ResponseCode
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RandomUserController(private val userService: UserService, private val redisService: RedisService) {
    private val logger = LoggerFactory.getLogger(RandomUserController::class.java)

    @GetMapping("/random", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRandomUser(@RequestParam(name = "seed", required = false) seed: String?): CommonResponse<Any> {
        logger.info("Start getRandomUser with: {}", seed)
        val response = seed?.let {
            redisService.cacheManagement({ userService.getRandomUser(it) }, { redisService.getUser(it) })
        } ?: userService.getRandomUser()

        return CommonResponse(
            ResponseCode.SUCCESS,
            response
        ).also { logger.info("End getRandomUser with: {}, response: {}", seed, it) }
    }
}