package com.example.infrastructure.persistence.user

import org.springframework.stereotype.Repository

import com.example.application.user.UserReadRepo
import com.example.domain.user.Email
import com.example.domain.user.UserId
import com.example.domain.user.UserModel

@Repository
class UserReadImMemRepo(
    private val userStorage: UserStorage,
) : UserReadRepo {
    override fun getAll(): Result<List<UserModel>> = Result.success(userStorage.users)

    override fun get(userId: UserId): Result<UserModel?> =
        Result.success(
            userStorage.users.find { user ->
                user.id ==
                    userId
            },
        )

    override fun findByEmail(email: Email): Result<UserModel?> =
        Result.success(
            userStorage.users.find { user ->
                user.email ==
                    email
            },
        )
}
