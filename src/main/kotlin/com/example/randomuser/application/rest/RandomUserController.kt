package com.example.randomuser.application.rest

import com.example.randomuser.application.response.CommonResponse
import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.domain.service.UserService
import com.example.randomuser.infrastracture.constants.ResponseCode
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RandomUserController(private val userService: UserService) {

    @GetMapping("/random-user", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRandomUser(@RequestParam(name = "seed", required = false) seed: String?): CommonResponse<UserResponse> {
        return CommonResponse(ResponseCode.SUCCESS, userService.getRandomUser(seed))
    }
}