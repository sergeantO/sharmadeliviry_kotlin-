package com.example.application.user

import com.example.domain.user.Email
import com.example.domain.user.UserId
import com.example.domain.user.UserModel

interface UserReadRepo {
    suspend fun getAll(): Result<List<UserModel>>

    suspend fun get(userId: UserId): Result<UserModel?>

    suspend fun findByEmail(email: Email): Result<UserModel?>
}
