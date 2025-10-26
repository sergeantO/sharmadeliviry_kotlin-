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
import com.example.infrastructure.shared.ResponseMapper

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val responseMapper: ResponseMapper,
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
            .let { result -> responseMapper.toResponseEntity(result) { users -> users.map { it.toDto() } } }

    @GetMapping("/{id}")
    suspend fun get(
        @PathVariable("id") id: UUID,
    ): ResponseEntity<UserDto> =
        getUserQuery
            .execute(UserId(id))
            .let { result -> responseMapper.toResponseEntity(result) { it -> it?.toDto() } }

    @PostMapping
    suspend fun create(
        @RequestBody userDto: CreateUserDto,
    ): ResponseEntity<UserDto> =
        createUserCommand
            .execute(userDto.toModel())
            .let { result -> responseMapper.toResponseEntity(result, HttpStatus.CREATED) { model -> model.toDto() } }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable("id") id: UUID,
        @RequestBody userDto: UpdateUserDto,
    ): ResponseEntity<UserDto> =
        updateUserCommand
            .execute(UserId(id), userDto.toModel())
            .let { result -> responseMapper.toResponseEntity(result) { it -> it?.toDto() } }

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
