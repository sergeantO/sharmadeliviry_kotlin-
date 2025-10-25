package com.example.application.user.query

import com.example.application.user.UserReadRepo
import com.example.domain.user.UserId
import com.example.domain.user.UserModel

class GetUserQuery(
    private val repo: UserReadRepo,
) {
    suspend fun execute(userId: UserId): Result<UserModel?> = repo.get(userId)
}
