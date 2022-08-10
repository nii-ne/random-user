package com.example.randomuser.application.rest

import com.example.randomuser.application.response.CommonResponse
import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.domain.service.RedisService
import com.example.randomuser.domain.service.UserService
import com.example.randomuser.infrastracture.constants.ResponseCode
import com.example.randomuser.infrastracture.exception.CommonAdvice
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.lang.NullPointerException


@ExtendWith(MockKExtension::class, RestDocumentationExtension::class, SpringExtension::class)
internal class RandomUserControllerTest {
    private lateinit var randomUserController: RandomUserController
    private lateinit var mockMvc: MockMvc
    private val redisService: RedisService = mockk()
    private val userService: UserService = mockk()
    private val objectMapper = ObjectMapper()
    private val userResponse = UserResponse("XXX","female","BKK","seed")

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        randomUserController = RandomUserController(userService, redisService)
        mockMvc = MockMvcBuilders.standaloneSetup(randomUserController).apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation)
            .uris().withScheme("https").withPort(8080)
        ).setControllerAdvice(CommonAdvice()).build()
    }
    @Test
    fun `should return user when getRandomUser with request param then success`() {
        every { redisService.getUser(any()) } returns userResponse
        mockMvc.perform(get("/api/random")
            .param("seed", "test"))

            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(CommonResponse(ResponseCode.SUCCESS, userResponse))))
            .andDo(
                document(
                    "random_user/success_with_request_param",
                    preprocessResponse(prettyPrint()),
                )
            )
        verify(exactly = 1) { redisService.getUser(any()) }
        verify(exactly = 0) { userService.getRandomUser()}
    }
    @Test
    fun `should return user when getRandomUser without request param then success`() {
        every { userService.getRandomUser() } returns userResponse
        mockMvc.perform(get("/api/random"))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(CommonResponse(ResponseCode.SUCCESS, userResponse))))
            .andDo(
                document(
                    "random_user/success",
                    preprocessResponse(prettyPrint()),
                )
            )

        verify(exactly = 0) { redisService.getUser(any()) }
        verify(exactly = 1) { userService.getRandomUser()}
    }
    @Test
    fun `should exception when getRandomUser then throw exception`() {
        every { userService.getRandomUser() } throws NullPointerException("test error")
        mockMvc.perform(get("/api/random"))
            .andExpect(status().isInternalServerError)
            .andExpect(content().json(objectMapper.writeValueAsString(CommonResponse<Any>(code = ResponseCode.GENERAL_ERROR.code, message = ResponseCode.GENERAL_ERROR.message, description = "test error"))))
            .andDo(
                document(
                    "random_user/general_error",
                    preprocessResponse(prettyPrint()),
                )
            )

        verify(exactly = 0) { redisService.getUser(any()) }
        verify(exactly = 1) { userService.getRandomUser()}
    }
}