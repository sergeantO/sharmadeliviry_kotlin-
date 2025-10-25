package com.example.application.user.command

import com.example.domain.TransactionManager
import com.example.domain.user.CreateUserModel
import com.example.domain.user.UserModel
import com.example.domain.user.UserWriteRepo

class CreateUserCommand(
    private val tm: TransactionManager,
    private val repo: UserWriteRepo,
) {
    suspend fun execute(user: CreateUserModel): Result<UserModel> = repo.create(tm, user)
}
