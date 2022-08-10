package com.example.randomuser.infrastracture.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
internal class StaticResourceConfiguration : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/docs/**")
            .addResourceLocations("classpath:/restdocs/docs/")
            .resourceChain(true)
            .addResolver(PathResourceResolver())
    }

}