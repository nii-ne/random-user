package com.example.randomuser.domain.service

import com.example.randomuser.application.response.UserResponse
import com.example.randomuser.infrastracture.configuration.RedisProperties
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.RedisConnectionFailureException
import org.springframework.data.redis.core.ValueOperations

@ExtendWith(MockKExtension::class)
class RedisServiceTest {
    @InjectMockKs
    private lateinit var redisService: RedisService
    @MockK
    private lateinit var userService: UserService
    @MockK
    private lateinit var redisProperties: RedisProperties

    @MockK("mainRedisTemplate")
    private lateinit var valueOperations: ValueOperations<String, UserResponse>
    private val userResponse = UserResponse("", "", "", "")

    @BeforeEach
    fun setUp() {
        every { redisProperties.ttl } returns 3
    }

    @Test
    fun `should return user from redis then success`() {
        every { valueOperations.get(any()) } returns userResponse
        val actual = redisService.getUser("seed")
        assertEquals(userResponse, actual)
        verify(exactly = 1) { valueOperations.get(any()) }
        verify(exactly = 0) { userService.getRandomUser(any()) }
        verify(exactly = 0) { valueOperations.set(any(), any(), any(), any()) }
    }
    @Test
    fun `should return user and save to redis when get from redis then not found`() {
        every { valueOperations.get(any()) } returns null
        every { userService.getRandomUser(any()) } returns userResponse
        every { valueOperations.set(any(), any(), any(), any()) } just Runs
        val actual = redisService.getUser("seed")
        assertEquals(userResponse, actual)
        verify(exactly = 1) { valueOperations.get(any()) }
        verify(exactly = 1) { userService.getRandomUser(any()) }
        verify(exactly = 1) { valueOperations.set(any(), any(), any(), any()) }
    }
    @Test
    fun `should return user and save to redis when get from redis then exception`() {
        every { valueOperations.get(any()) } throws RedisConnectionFailureException("error")
        every { userService.getRandomUser(any()) } returns userResponse
        every { valueOperations.set(any(), any(), any(), any()) } just Runs
        val actual = redisService.getUser("seed")
        assertEquals(userResponse, actual)
        verify(exactly = 1) { valueOperations.get(any()) }
        verify(exactly = 1) { userService.getRandomUser(any()) }
        verify(exactly = 1) { valueOperations.set(any(), any(), any(), any()) }
    }
    @Test
    fun `should return user when get from redis then not found and save to redis failed`() {
        every { valueOperations.get(any()) } returns null
        every { userService.getRandomUser(any()) } returns userResponse
        every { valueOperations.set(any(), any(), any(), any()) } throws RedisConnectionFailureException("error")
        val actual = redisService.getUser("seed")
        assertEquals(userResponse, actual)
        verify(exactly = 1) { valueOperations.get(any()) }
        verify(exactly = 1) { userService.getRandomUser(any()) }
        verify(exactly = 1) { valueOperations.set(any(), any(), any(), any()) }
    }
}