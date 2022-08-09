package com.example.randomuser.infrastracture.exception

import com.example.randomuser.infrastracture.constants.ResponseCode
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.http.HttpStatus.Series.CLIENT_ERROR
import org.springframework.http.HttpStatus.Series.SERVER_ERROR

@Component
class RestTemplateResponseErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = response.statusCode.series() == CLIENT_ERROR || response.statusCode.series() == SERVER_ERROR

    override fun handleError(response: ClientHttpResponse) {
        val status = response.statusCode
        val series = status.series()
        if (validateClientServerError(series)) {
            when (status) {
                HttpStatus.BAD_REQUEST -> throw CommonException(ResponseCode.BAD_REQUEST)
                HttpStatus.NOT_FOUND -> throw CommonException(ResponseCode.NOT_FOUND)
                HttpStatus.REQUEST_TIMEOUT -> throw CommonException(ResponseCode.TIMEOUT)
                HttpStatus.SERVICE_UNAVAILABLE -> throw CommonException(ResponseCode.SERVICE_UNAVAILABLE)
                else -> throw CommonException(ResponseCode.GENERAL_ERROR)
            }
        }
    }

    protected fun validateClientServerError(series: HttpStatus.Series): Boolean {
        return series == CLIENT_ERROR || series == SERVER_ERROR
    }
}