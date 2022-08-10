package com.example.randomuser.domain.client

import com.example.randomuser.domain.model.response.Info
import com.example.randomuser.domain.model.response.UserInfo
import com.example.randomuser.infrastracture.configuration.RandomUserProperties
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

internal class RandomClientTest {
    private lateinit var randomClient: RandomClient
    private val randomUserRestTemplate: RestTemplate = mockk()
    private val randomUserProperties: RandomUserProperties = mockk()

    @BeforeEach
    fun setUp() {
        randomClient = RandomClient(randomUserRestTemplate, randomUserProperties)
        every { randomUserProperties.endpoints["protocol"] } returns "http"
        every { randomUserProperties.endpoints["base"] } returns "localhost"
        every { randomUserProperties.endpoints["path"] } returns "/api"
    }

    @Test
    fun `should return userInfo when random user then success`() {
        val expected = UserInfo(
            info = Info(0, 0, "seed", "version"),
            results = emptyList()
        )
        every {
            randomUserRestTemplate.exchange(
                any<String>(),
                any(),
                any(),
                any<ParameterizedTypeReference<UserInfo>>()
            )
        } returns ResponseEntity.ok(expected)
        val actual = randomClient.getRandomUser("")
        assertEquals(expected, actual)
        verify(exactly = 1) {
            randomUserRestTemplate.exchange(
                any<String>(),
                any(),
                any(),
                any<ParameterizedTypeReference<UserInfo>>()
            )
        }
    }

    @Test
    fun `should throw exception when random user then failure`() {
        every {
            randomUserRestTemplate.exchange(
                any<String>(),
                any(),
                any(),
                any<ParameterizedTypeReference<UserInfo>>()
            )
        } throws Exception()
        assertThrows<Exception> { randomClient.getRandomUser("") }
        verify(exactly = 1) {
            randomUserRestTemplate.exchange(
                any<String>(),
                any(),
                any(),
                any<ParameterizedTypeReference<UserInfo>>()
            )
        }
    }
}