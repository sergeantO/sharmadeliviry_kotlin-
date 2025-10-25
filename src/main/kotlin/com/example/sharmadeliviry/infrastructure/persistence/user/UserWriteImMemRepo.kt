package com.example.infrastructure.persistence.user

import java.util.UUID
import org.springframework.stereotype.Repository

import com.example.domain.TransactionManager
import com.example.domain.user.CreateUserModel
import com.example.domain.user.UpdateUserModel
import com.example.domain.user.UserId
import com.example.domain.user.UserModel
import com.example.domain.user.UserWriteRepo
import com.example.infrastructure.persistence.user.UserStorage

@Repository
class UserWriteImMemRepo(
    private val userStorage: UserStorage,
) : UserWriteRepo {
    override fun get(
        tm: TransactionManager,
        userId: UserId,
    ): Result<UserModel?> = Result.success(userStorage.users.find { it.id == userId })

    override fun create(
        tm: TransactionManager,
        user: CreateUserModel,
    ): Result<UserModel> {
        val id = UserId(UUID.randomUUID())
        val newUser =
            UserModel(
                id = id,
                username = user.username,
                email = user.email,
            )
        userStorage.users.add(newUser)
        return Result.success(newUser)
    }

    override fun update(
        tm: TransactionManager,
        id: UserId,
        user: UpdateUserModel,
    ): Result<UserModel?> {
        val foundIdx = userStorage.users.indexOfFirst { it.id == id }
        if (foundIdx == -1) return Result.success(null)

        val oldUser = userStorage.users[foundIdx]
        val newUser =
            UserModel(
                id = id,
                username = oldUser.username,
                email = user.email ?: oldUser.email,
            )
        userStorage.users[foundIdx] = newUser
        return Result.success(newUser)
    }

    override fun delete(
        tm: TransactionManager,
        id: UserId,
    ): Result<Boolean> {
        val user = userStorage.users.find { it.id == id }
        userStorage.users.remove(user)
        return Result.success(true)
    }

    override fun cleanup(tm: TransactionManager): Result<Boolean> {
        userStorage.users.clear()
        return Result.success(true)
    }
}
