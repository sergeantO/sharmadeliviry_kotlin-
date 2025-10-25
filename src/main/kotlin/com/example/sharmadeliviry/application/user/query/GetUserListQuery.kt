package com.example.application.user.query

import com.example.application.user.UserReadRepo
import com.example.domain.user.UserModel

class GetUserListQuery(
    private val repo: UserReadRepo,
) {
    suspend fun execute(): Result<List<UserModel>> = repo.getAll()
}
