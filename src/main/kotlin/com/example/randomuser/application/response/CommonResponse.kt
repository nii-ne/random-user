package com.example.randomuser.application.response

import com.example.randomuser.infrastracture.constants.Namespace
import com.example.randomuser.infrastracture.constants.ResponseCode

data class CommonResponse<T>(
    val namespace: String = Namespace.RANDOM_USER_SERVICE,
    val code: String,
    val message: String,
    val description: String? = null,
    val data: T? = null
) {
    constructor(response: ResponseCode, data: T? = null) : this(code = response.code, message = response.message, data = data)
    constructor(response: ResponseCode, description: String?, data: T? = null) : this(code = response.code, message = response.message, description = description, data = data)
}
