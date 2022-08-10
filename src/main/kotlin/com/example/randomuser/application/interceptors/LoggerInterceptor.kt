package com.example.randomuser.application.interceptors

import com.example.randomuser.infrastracture.constants.CommonConstant.X_CORRELATION_ID
import com.example.randomuser.infrastracture.constants.Namespace
import org.apache.logging.log4j.ThreadContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.AsyncHandlerInterceptor
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class LoggerInterceptor : AsyncHandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var xid: String? = request.getHeader(X_CORRELATION_ID)

        if (xid.isNullOrBlank()) {
            xid = "${Namespace.RANDOM_USER_SERVICE}-${UUID.randomUUID().toString().replace("-", "").lowercase()}"
            request.setAttribute(X_CORRELATION_ID, xid)
        }
        ThreadContext.put(X_CORRELATION_ID, xid)
        return true
    }

}