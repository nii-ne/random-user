package com.example.randomuser.infrastracture.constants

import org.springframework.http.HttpStatus

enum class ResponseCode(val httpStatus: HttpStatus, val code: String, val message: String) {
    SUCCESS(HttpStatus.OK, "200-000", "Success"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "204-000", "No Content"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400-000", "Invalid parameter"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404-000", "Not found"),
    TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "408-000", "Timeout"),
    GENERAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500-000", "Unexpected error"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "503-000", "Service unavailable"),
}