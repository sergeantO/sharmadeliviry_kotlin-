package com.example.application.user

import com.example.domain.user.Email
import com.example.domain.user.UserId
import com.example.domain.user.UserModel

interface UserReadRepo {
    fun getAll(): Result<List<UserModel>>

    fun get(userId: UserId): Result<UserModel?>

    fun findByEmail(email: Email): Result<UserModel?>
}
