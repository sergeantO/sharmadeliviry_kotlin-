package com.example.infrastructure.controller

import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import com.example.application.user.command.CreateUserCommand
import com.example.application.user.command.DeleteUserCommand
import com.example.application.user.command.UpdateUserCommand
import com.example.application.user.query.GetUserListQuery
import com.example.application.user.query.GetUserQuery
import com.example.domain.user.CreateUserModel
import com.example.domain.user.Email
import com.example.domain.user.UpdateUserModel
import com.example.domain.user.UserId
import com.example.domain.user.UserModel

inline fun <T, R> Result<T>.toResponseEntity(
    successStatus: HttpStatus,
    successTransform: (T) -> R?,
): ResponseEntity<R> =
    when {
        isSuccess -> {
            val result = successTransform(getOrNull()!!)
            if (result != null) {
                ResponseEntity.status(successStatus).body(result)
            } else {
                ResponseEntity.notFound().build()
            }
        }
        else -> throw mapToException(exceptionOrNull())
    }

inline fun <T, R> Result<T>.toResponseEntity(successTransform: (T) -> R?): ResponseEntity<R> =
    toResponseEntity(HttpStatus.OK, successTransform)

fun mapToException(exception: Throwable?): Exception =
    when (exception) {
        else -> RuntimeException("Operation failed", exception)
    }



@RestController
@RequestMapping("/users")
class UserController(
    private val getUserQuery: GetUserQuery,
    private val getUserListQuery: GetUserListQuery,
    private val createUserCommand: CreateUserCommand,
    private val updateUserCommand: UpdateUserCommand,
    private val deleteUserCommand: DeleteUserCommand,
) {
    @GetMapping
    suspend fun list(): ResponseEntity<List<UserDto>> =
        getUserListQuery
            .execute()
            .toResponseEntity { users -> users.map { it.toDto() } }

    @GetMapping("/{id}")
    suspend fun get(
        @PathVariable("id") id: UUID,
    ): ResponseEntity<UserDto> =
        getUserQuery
            .execute(UserId(id))
            .toResponseEntity { it -> it?.toDto() }

    @PostMapping
    suspend fun create(
        @RequestBody userDto: CreateUserDto,
    ): ResponseEntity<UserDto> =
        createUserCommand
            .execute(userDto.toModel())
            .toResponseEntity(HttpStatus.CREATED) { model -> model.toDto() }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable("id") id: UUID,
        @RequestBody userDto: UpdateUserDto,
    ): ResponseEntity<UserDto> =
        updateUserCommand
            .execute(UserId(id), userDto.toModel())
            .toResponseEntity { model -> model?.toDto() }

    @DeleteMapping("/{id}")
    suspend fun delete(
        @PathVariable("id") id: UUID,
    ): ResponseEntity<Unit> =
        deleteUserCommand
            .execute(UserId(id))
            .let { ResponseEntity.noContent().build() }
}

data class UserDto(
    val id: String,
    val username: String,
    val email: String,
)

private fun UserModel.toDto(): UserDto =
    UserDto(
        id = this.id.value.toString(),
        username = this.username,
        email = this.email.value,
    )

data class CreateUserDto(
    val username: String,
    val email: String,
    val password: String,
)

private fun CreateUserDto.toModel() =
    CreateUserModel(
        username = username,
        email = Email(email),
        password = password,
    )

data class UpdateUserDto(
    val username: String?,
    val email: String?,
    val password: String?,
)

private fun UpdateUserDto.toModel() =
    UpdateUserModel(
        username = username,
        email = email?.let { Email(it) },
        password = password,
    )
