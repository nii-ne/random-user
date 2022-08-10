package com.example.randomuser.application.interceptors

import com.example.randomuser.infrastracture.constants.CommonConstant.X_CORRELATION_ID
import com.example.randomuser.infrastracture.constants.Namespace
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.apache.logging.log4j.ThreadContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.method.HandlerMethod
import java.util.UUID

@ExtendWith(MockKExtension::class)
class LoggerInterceptorTest {

    @InjectMockKs
    private lateinit var loggerInterceptor: LoggerInterceptor
    private val request = MockHttpServletRequest()
    private val response = MockHttpServletResponse()
    private val handler = mockk<HandlerMethod>()
    @Test
    fun `should save CorrelationId when preHandle success`() {
        val cid = UUID.randomUUID().toString()
        request.addHeader(X_CORRELATION_ID, cid)
        loggerInterceptor.preHandle(request, response, handler)
        val actual = ThreadContext.get(X_CORRELATION_ID)
        assertEquals(cid, actual)
    }
    @Test
    fun `should save CorrelationId when preHandle success get correlation id is blank`() {
        request.addHeader(X_CORRELATION_ID, "")
        loggerInterceptor.preHandle(request, response, handler)
        val actual = ThreadContext.get(X_CORRELATION_ID)
        assertTrue(actual.contains(Namespace.RANDOM_USER_SERVICE))
    }
    @Test
    fun `should save CorrelationId when preHandle success get correlation id is empty`() {
        request.addHeader(X_CORRELATION_ID, "")
        loggerInterceptor.preHandle(request, response, handler)
        val actual = ThreadContext.get(X_CORRELATION_ID)
        assertTrue(actual.contains(Namespace.RANDOM_USER_SERVICE))
    }
    @Test
    fun `should save CorrelationId when preHandle success get correlation id is null`() {
        loggerInterceptor.preHandle(request, response, handler)
        val actual = ThreadContext.get(X_CORRELATION_ID)
        assertTrue(actual.contains(Namespace.RANDOM_USER_SERVICE))
    }
}