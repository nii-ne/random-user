package com.example.randomuser.infrastracture.exception

import com.example.randomuser.infrastracture.constants.ResponseCode
import java.lang.Exception

class CommonException(val responseCode: ResponseCode, val exception: Exception? = null): RuntimeException()