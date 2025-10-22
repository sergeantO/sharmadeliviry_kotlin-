package com.example.domain.user

interface UserRepo {
    /**
     * сохранить пользователя
     * @param user
     */
    fun save(user: UserModel): UserModel
    fun delete(user: UserModel): UserModel

    /** удалить всех пользователей */
    fun cleanup(): Boolean

    fun get(userId: Long): UserModel?
    fun update(user: UserModel): UserModel?
    fun findByEmail(email: String): UserModel?
}
