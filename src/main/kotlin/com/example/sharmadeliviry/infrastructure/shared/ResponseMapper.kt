package com.example.infrastructure.shared

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ResponseMapper(
    private val exceptionMapper: ExceptionMapper,
) {
    fun <T> toResponseEntity(result: Result<T>): ResponseEntity<T> =
        result.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { exceptionMapper.mapException(it) },
        )

    fun <T, R> toResponseEntity(
        result: Result<T>,
        transform: (T) -> R,
    ): ResponseEntity<R> = toResponseEntity(result.map(transform))

    fun <T> toResponseEntity(
        result: Result<T>,
        onSuccess: (T) -> ResponseEntity<T>,
    ): ResponseEntity<T> =
        result.fold(
            onSuccess = { onSuccess(it) },
            onFailure = { exceptionMapper.mapException(it) },
        )
}
