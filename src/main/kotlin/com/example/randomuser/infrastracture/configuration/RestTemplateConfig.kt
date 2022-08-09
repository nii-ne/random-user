package com.example.randomuser.infrastracture.configuration

import com.example.randomuser.infrastracture.exception.RestTemplateResponseErrorHandler
import com.example.randomuser.infrastracture.interceptors.RestTemplateHeaderInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.protocol.HTTP
import org.apache.http.ssl.SSLContexts
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLSession

@Configuration
class RestTemplateConfig(private val randomUserProperties: RandomUserProperties) {
    @Bean(name = ["clientErrorHandler"])
    fun clientErrorHandler(): ResponseErrorHandler {
        return RestTemplateResponseErrorHandler()
    }
    @Bean(name = ["randomUserRestTemplate"])
    @Primary
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    fun randomUserRestTemplate(@Qualifier("clientErrorHandler") clientErrorHandler: ResponseErrorHandler): RestTemplate {
        val restTemplate = makeRestTemplate(
            randomUserProperties.maxTotal as Int,
            randomUserProperties.defaultMaxPerRoute as Int,
            randomUserProperties.connectTimeout as Int,
            randomUserProperties.socketTimeout as Int,
            randomUserProperties.keepAliveHeader as Int)
        restTemplate.errorHandler = clientErrorHandler
        return restTemplate
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun makeRestTemplate(
        maxTotal: Int,
        maxPerRoute: Int,
        connectionTimeout: Int,
        socketTimeout: Int,
        keepAliveHeader: Int
    ): RestTemplate {

        val poolingHttpClientConnectionManager = poolingHttpClientConnectionManager(maxTotal, maxPerRoute)

        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(connectionTimeout)
            .setSocketTimeout(socketTimeout)
            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
            .build()

        val closeableHttpClient = HttpClients.custom()
            .setConnectionManager(poolingHttpClientConnectionManager)
            .setKeepAliveStrategy(connectionKeepAliveStrategy(keepAliveHeader))
            .setDefaultRequestConfig(requestConfig)
            .build()

        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(closeableHttpClient)
        val restTemplate = RestTemplate()
        restTemplate.requestFactory = clientHttpRequestFactory
        restTemplate.interceptors = getInterceptors()
        return restTemplate
    }

    fun connectionKeepAliveStrategy(keepAliveHeader: Int): ConnectionKeepAliveStrategy {
        return ConnectionKeepAliveStrategy { response, _ ->
            val it = BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE))
            while (it.hasNext()) {
                val he = it.nextElement()
                val param = he.name
                val value = he.value
                if ((value != null && param.equals("timeout", ignoreCase = true))) {
                    return@ConnectionKeepAliveStrategy value.toLong() * 1000
                }
            }
            (keepAliveHeader * 1000).toLong()
        }
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun poolingHttpClientConnectionManager(maxTotal: Int, maxPerRoute: Int): PoolingHttpClientConnectionManager {
        val acceptingTrustStrategy = { _: Array<X509Certificate>, _: String -> true }
        val allPassVerifier = { _: String, _: SSLSession -> true } // ignore hostname checking

        val sslContext = SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy)
            .build() // keystore is null, not keystore is used at all

        val csf = SSLConnectionSocketFactory(sslContext, allPassVerifier)

        val socketFactoryRegistry = RegistryBuilder.create<ConnectionSocketFactory>()
            .register("https", csf)
            .register("http", PlainConnectionSocketFactory())
            .build()

        val poolingHttpClientConnectionManager = PoolingHttpClientConnectionManager(socketFactoryRegistry)

        poolingHttpClientConnectionManager.maxTotal = maxTotal
        poolingHttpClientConnectionManager.defaultMaxPerRoute = maxPerRoute
        return poolingHttpClientConnectionManager
    }

    private fun getInterceptors(): List<ClientHttpRequestInterceptor> {
        return listOf(RestTemplateHeaderInterceptor())
    }

}