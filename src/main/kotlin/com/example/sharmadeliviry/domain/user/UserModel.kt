package com.example.domain.user

import java.util.UUID

@JvmInline value class UserId(
    val value: UUID,
)

@JvmInline value class Email(
    val value: String,
)

data class UserModel(
    val id: UserId,
    val username: String,
    val email: Email,
)

data class CreateUserModel(
    val username: String,
    val email: Email,
    val password: String,
)

data class UpdateUserModel(
    val username: String?,
    val email: Email?,
    val password: String?,
)
