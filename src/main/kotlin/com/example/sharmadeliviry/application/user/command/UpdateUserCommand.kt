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
    ): Result<UserModel?> =
        tm.inTransaction({
            val foundUser = repo.get(id)
            if (foundUser.isFailure) {
                Result.success(null)
            }

            repo.update(id, user)
        })
}
