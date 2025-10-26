package com.example.application.user.command

import com.example.domain.TransactionManager
import com.example.domain.user.UpdateUserModel
import com.example.domain.user.UserId
import com.example.domain.user.UserModel
import com.example.domain.user.UserWriteRepo

class UpdateUserCommand(
    private val tm: TransactionManager,
    private val repo: UserWriteRepo,
) {
    suspend fun execute(
        id: UserId,
        user: UpdateUserModel,
    ): Result<UserModel?> {
        val foundUser = repo.get(tm, id)
        if (foundUser.isFailure) return Result.success(null)

        return repo.update(tm, id, user)
    }
}
