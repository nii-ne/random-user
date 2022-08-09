package com.example.randomuser.infrastracture.exception

import com.example.randomuser.application.response.CommonResponse
import com.example.randomuser.infrastracture.constants.ResponseCode
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception
import org.slf4j.LoggerFactory

@RestControllerAdvice
class CommonAdvice : ResponseEntityExceptionHandler() {
    private val logger = LoggerFactory.getLogger(CommonAdvice::class.java)
    private fun buildResponseEntity(
        responseCode: ResponseCode,
        ex: Exception
    ): ResponseEntity<Any> {
        return ResponseEntity.status(responseCode.httpStatus).body(
            CommonResponse(
                response = responseCode,
                description = ex.message,
                data = null
            )
        )
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("handleExceptionInternal: {}", ex)
        return buildResponseEntity(ResponseCode.GENERAL_ERROR, ex)
    }

    @ExceptionHandler(CommonException::class)
    protected fun handleCommonException(ex: CommonException): ResponseEntity<Any> {
        logger.error("handleCommonException: {}", ex)
        return buildResponseEntity(ex.responseCode, ex)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(ex: Exception): ResponseEntity<Any> {
        logger.error("handleException: {}", ex)
        return buildResponseEntity(ResponseCode.GENERAL_ERROR, ex)
    }
}