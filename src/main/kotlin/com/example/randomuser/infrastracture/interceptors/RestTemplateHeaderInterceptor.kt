package com.example.randomuser.infrastracture.interceptors

import com.example.randomuser.infrastracture.constants.CommonConstant
import com.example.randomuser.infrastracture.constants.Namespace
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import java.util.UUID

class RestTemplateHeaderInterceptor : ClientHttpRequestInterceptor {

    private val httpServletRequest: HttpServletRequest
        get() {
            val servletRequestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            return servletRequestAttributes.request
        }

    private val correlationId: String
        get() {
            return try {
                val id = httpServletRequest.getHeader(CommonConstant.X_CORRELATION_ID)
                if (id.isNullOrBlank())
                    httpServletRequest.getAttribute(CommonConstant.X_CORRELATION_ID) as String
                else
                    id
            } catch (e: Exception) {
                val transactionId = UUID.randomUUID().toString()
                "${Namespace.RANDOM_USER_SERVICE}-${transactionId.replace("-", "").lowercase()}"
            }
        }

    @Throws(IOException::class)
    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        val headers = request.headers
        headers.set(HttpHeaders.CACHE_CONTROL, "no-cache")
        headers.set(CommonConstant.X_CORRELATION_ID, correlationId)
        return execution.execute(request, body)
    }
}
