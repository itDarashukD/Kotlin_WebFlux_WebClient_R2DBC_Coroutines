package com.epam.mentoring.kotlin.handler

import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters


@Component
@Order(-2)
class GlobalErrorWebExceptionHandler (
    errorAttributes: ErrorAttributes,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes,WebProperties.Resources(), applicationContext) {

    init {
       setMessageWriters(serverCodecConfigurer.writers);
       setMessageReaders(serverCodecConfigurer.readers);
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        val status = HttpStatus.valueOf(errorPropertiesMap["status"] as Int)

        val errorResponse: ErrorResponse =
            when (status) {
                HttpStatus.BAD_REQUEST -> ErrorResponse(status.value(), "Bad Request", "The request is invalid.")
                HttpStatus.NOT_FOUND -> ErrorResponse(status.value(), "Not Found", "The requested resource was not found.")
                HttpStatus.INTERNAL_SERVER_ERROR -> ErrorResponse(status.value(), "Internal Server Error", "An unexpected error occurred.")

                else -> ErrorResponse(status.value(), "Error", errorPropertiesMap["message"].toString())
        }

        return ServerResponse.status(status) .bodyValue(errorResponse)
    }


}