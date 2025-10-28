package com.example.infrastructure.persistence.user

import java.util.UUID
import org.springframework.stereotype.Repository

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
    override suspend fun getNextId(): UserId = UserId(UUID.randomUUID())

    override suspend fun get(userId: UserId): Result<UserModel?> =
        Result.success(userStorage.items.find { it.id == userId })

    override suspend fun create(user: CreateUserModel): Result<UserModel> {
        val id = UserId(UUID.randomUUID())
        val newUser =
            UserModel(
                id = id,
                username = user.username,
                email = user.email,
            )
        userStorage.items.add(newUser)
        return Result.success(newUser)
    }

    override suspend fun update(
        id: UserId,
        user: UpdateUserModel,
    ): Result<UserModel?> {
        val foundIdx = userStorage.items.indexOfFirst { it.id == id }
        if (foundIdx == -1) return Result.success(null)

        val oldUser = userStorage.items[foundIdx]
        val newUser =
            UserModel(
                id = id,
                username = oldUser.username,
                email = user.email ?: oldUser.email,
            )
        userStorage.items[foundIdx] = newUser
        return Result.success(newUser)
    }

    override suspend fun delete(id: UserId): Result<Boolean> {
        val user = userStorage.items.find { it.id == id }
        userStorage.items.remove(user)
        return Result.success(true)
    }

    override suspend fun cleanup(): Result<Boolean> {
        userStorage.items.clear()
        return Result.success(true)
    }
}
