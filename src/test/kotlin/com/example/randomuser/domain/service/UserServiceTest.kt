package com.example.randomuser.domain.service

import com.example.randomuser.domain.client.RandomClient
import com.example.randomuser.domain.model.response.*
import com.example.randomuser.infrastracture.constants.ResponseCode
import com.example.randomuser.infrastracture.exception.CommonException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class UserServiceTest {
    private lateinit var userService: UserService
    private val randomClient: RandomClient = mockk()

    @BeforeEach
    fun setUp() {
        userService = UserService(randomClient)
    }

    @Test
    fun `should return user when getRandomUser then success`() {
        val userInfo = UserInfo(
            Info(1, 1, "seed", "version"),
            listOf(
                Result(
                    "cell",
                    Dob(20, "1980-01-01"),
                    "email", "location",
                    Id("name", "value"),
                    Location(
                        "city",
                        Coordinates("latitude", "longitude"),
                        "country", "postcode", "state",
                        Street("name", 0),
                        Timezone("description", "offset")
                    ),
                    Login("md5", "password", "salt", "sha1", "sha256", "username", "uuid"),
                    Name("first", "last", "title"),
                    "nat",
                    "phone",
                    Picture("large", "medium", "thumbnail"),
                    Registered(20, "1980-01-01"),
                )
            )
        )
        every { randomClient.getRandomUser(any()) } returns userInfo
        val actual = userService.getRandomUser("")
        assertEquals(
            "${userInfo.results[0].name.title} ${userInfo.results[0].name.first} ${userInfo.results[0].name.last}",
            actual.name
        )
        assertEquals(userInfo.results[0].gender, actual.gender)
        assertEquals(
            "street ${userInfo.results[0].location.street.number} ${userInfo.results[0].location.street.name}, ${userInfo.results[0].location.city}, ${userInfo.results[0].location.state} ${userInfo.results[0].location.postcode} ${userInfo.results[0].location.country}",
            actual.address
        )
        assertEquals(userInfo.info.seed, actual.seed)
        verify(exactly = 1) { randomClient.getRandomUser(any()) }
    }

    @Test
    fun `should throw common exception no content when getRandomUser then randomClient return null`() {
        every { randomClient.getRandomUser(any()) } returns null
        val actual = assertThrows<CommonException> { userService.getRandomUser("") }
        assertEquals(ResponseCode.NO_CONTENT.message, actual.responseCode.message)
        assertEquals(ResponseCode.NO_CONTENT.code, actual.responseCode.code)
        verify(exactly = 1) { randomClient.getRandomUser(any()) }
    }

    @Test
    fun `should throw exception when getRandomUser then randomClient exception`() {
        every { randomClient.getRandomUser(any()) } throws NullPointerException()
        assertThrows<NullPointerException> { userService.getRandomUser("") }
        verify(exactly = 1) { randomClient.getRandomUser(any()) }
    }

}