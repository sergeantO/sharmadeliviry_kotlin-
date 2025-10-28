package com.example.domain.user

interface UserWriteRepo {
    /** Сгенерировать новый id */
    suspend fun getNextId(): UserId

    /** Получить запись по id */
    suspend fun get(userId: UserId): Result<UserModel?>

    /** Создать запись */
    suspend fun create(user: CreateUserModel): Result<UserModel>

    /** Обновить запись */
    suspend fun update(
        id: UserId,
        user: UpdateUserModel,
    ): Result<UserModel?>

    /** Удалить запись */
    suspend fun delete(id: UserId): Result<Boolean>

    /** Очистить репозиторий */
    suspend fun cleanup(): Result<Boolean>
}
