package com.example.infrastructure.shared

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ResponseMapper(
    private val exceptionMapper: ExceptionMapper,
) {
    fun <T, R> toResponseEntity(
        result: Result<T>,
        successTransform: (T) -> R?,
    ): ResponseEntity<R> = toResponseEntity(result, HttpStatus.OK, successTransform)

    fun <T, R> toResponseEntity(
        result: Result<T>,
        successStatus: HttpStatus,
        successTransform: (T) -> R?,
    ): ResponseEntity<R> =
        when {
            result.isSuccess -> {
                val res = successTransform(result.getOrNull()!!)
                if (res != null) {
                    ResponseEntity.status(successStatus).body(res)
                } else {
                    ResponseEntity.notFound().build()
                }
            }
            else -> exceptionMapper.mapException(result.exceptionOrNull())
        }
}
