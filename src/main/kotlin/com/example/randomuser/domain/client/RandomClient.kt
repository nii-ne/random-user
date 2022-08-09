package com.example.randomuser.domain.client

import com.example.randomuser.domain.model.response.UserInfo
import com.example.randomuser.infrastracture.configuration.RandomUserProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class RandomClient(@Qualifier("randomUserRestTemplate") private val randomUserRestTemplate: RestTemplate, private val randomUserProperties: RandomUserProperties) {
    private val logger = LoggerFactory.getLogger(RandomClient::class.java)
    fun getRandomUser(seed: String?): UserInfo? {
        val uriComponents = UriComponentsBuilder.newInstance().scheme(randomUserProperties.endpoints["protocol"]).host(randomUserProperties.endpoints["base"]).path(randomUserProperties.endpoints["path"]!!).queryParam("seed", seed).build()
        val response = randomUserRestTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, null, object :ParameterizedTypeReference<UserInfo>(){})
        return response.body.also { logger.info("Response: $it") }
    }

}