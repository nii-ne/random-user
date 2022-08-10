package com.example.randomuser.infrastracture.exception

import com.example.randomuser.infrastracture.constants.ResponseCode
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.springframework.http.HttpStatus
import org.springframework.mock.http.client.MockClientHttpResponse

@ExtendWith(MockKExtension::class)
class RestTemplateResponseErrorHandlerTest {
    private val clientErrorHandler = RestTemplateResponseErrorHandler()

    @Test
    fun `should throws data not found when handleError 404`() {
        val httpStatus = HttpStatus.NOT_FOUND
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), httpStatus)
        val actual = assertThrows<CommonException> { clientErrorHandler.handleError(clientHttpResponse) }

        assertAll(
            Executable { assertEquals(httpStatus, actual.responseCode.httpStatus) },
            Executable { assertEquals(ResponseCode.NOT_FOUND.code, actual.responseCode.code) },
            Executable { assertEquals(ResponseCode.NOT_FOUND.message, actual.responseCode.message) }
        )
    }

    @Test
    fun `should throws service unavailable when handleError 503`() {
        val httpStatus = HttpStatus.SERVICE_UNAVAILABLE
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), httpStatus)
        val actual = assertThrows<CommonException> { clientErrorHandler.handleError(clientHttpResponse) }

        assertAll(
            Executable { assertEquals(httpStatus, actual.responseCode.httpStatus) },
            Executable { assertEquals(ResponseCode.SERVICE_UNAVAILABLE.code, actual.responseCode.code) },
            Executable { assertEquals(ResponseCode.SERVICE_UNAVAILABLE.message, actual.responseCode.message) }
        )
    }

    @Test
    fun `should throws request timeout when handleError 408`() {
        val httpStatus = HttpStatus.REQUEST_TIMEOUT
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), httpStatus)
        val actual = assertThrows<CommonException> { clientErrorHandler.handleError(clientHttpResponse) }

        assertAll(
            Executable { assertEquals(httpStatus, actual.responseCode.httpStatus) },
            Executable { assertEquals(ResponseCode.TIMEOUT.message, actual.responseCode.message) },
            Executable { assertEquals(ResponseCode.TIMEOUT.code, actual.responseCode.code) }
        )
    }

    @Test
    fun `should throws internal service error when handleError 500`() {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), httpStatus)
        val actual = assertThrows<CommonException> { clientErrorHandler.handleError(clientHttpResponse) }

        assertAll(
            Executable { assertEquals(httpStatus, actual.responseCode.httpStatus) },
            Executable { assertEquals(ResponseCode.GENERAL_ERROR.message, actual.responseCode.message) },
            Executable { assertEquals(ResponseCode.GENERAL_ERROR.code, actual.responseCode.code) }
        )
    }

    @Test
    fun `should exception unexpected error when handleError`() {
        val httpStatus = HttpStatus.GATEWAY_TIMEOUT
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), httpStatus)
        val actual = assertThrows<CommonException> { clientErrorHandler.handleError(clientHttpResponse) }

        assertAll(
            Executable { assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.responseCode.httpStatus) },
            Executable { assertEquals(ResponseCode.GENERAL_ERROR.message, actual.responseCode.message) },
            Executable { assertEquals(ResponseCode.GENERAL_ERROR.code, actual.responseCode.code) }
        )
    }

    @Test
    fun `should return true when http status is server`() {
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), HttpStatus.INTERNAL_SERVER_ERROR)
        assertTrue(clientErrorHandler.hasError(clientHttpResponse))
    }

    @Test
    fun `should return true when http status is client`() {
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), HttpStatus.REQUEST_TIMEOUT)
        assertTrue(clientErrorHandler.hasError(clientHttpResponse))
    }

    @Test
    fun `should return true when http status is other`() {
        val clientHttpResponse = MockClientHttpResponse(byteArrayOf(), HttpStatus.FOUND)
        assertFalse(clientErrorHandler.hasError(clientHttpResponse))
    }
}