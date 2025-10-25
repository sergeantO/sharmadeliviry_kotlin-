package com.example.application.user.command

import com.example.domain.TransactionManager
import com.example.domain.user.UserId
import com.example.domain.user.UserWriteRepo

class DeleteUserCommand(
    private val tm: TransactionManager,
    private val repo: UserWriteRepo,
) {
    suspend fun execute(id: UserId): Result<Boolean> = repo.delete(tm, id)
}
