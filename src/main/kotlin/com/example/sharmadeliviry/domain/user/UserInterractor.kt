package com.example.domain.user

open class UserInterractor(private val repo: UserRepo) {
    fun createUser(user: UserModel): UserModel = repo.save(user)
    fun updateUser(user: UserModel): UserModel? = repo.update(user)
    fun deleteUser(user: UserModel): UserModel = repo.delete(user)

    fun findUserByEmail(email: String): UserModel? = repo.findByEmail(email)
}
