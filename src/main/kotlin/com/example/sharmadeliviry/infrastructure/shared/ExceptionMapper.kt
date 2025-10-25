package com.example.infrastructure.shared

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

import com.example.shared.error.NotFoundException
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory

@Component
class ExceptionMapper {
    fun <T> mapException(exception: Throwable): ResponseEntity<T> =
        when (exception) {
            is NotFoundException ->
                ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponse(exception.message)) as ResponseEntity<T>
            is ValidationException ->
                ResponseEntity
                    .badRequest()
                    .body(ErrorResponse(exception.message)) as ResponseEntity<T>
            is AccessDeniedException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
            else -> {
                logger.error("Unhandled exception", exception)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }

    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionMapper::class.java)
    }
}

data class ErrorResponse(
    val message: String?,
    val timestamp: Long = System.currentTimeMillis(),
)
