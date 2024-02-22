package com.epam.mentoring.kotlin.util

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer



@Configuration
open class SwaggerConfig : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/", "/swagger-ui.html")
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui.html")
    }


}