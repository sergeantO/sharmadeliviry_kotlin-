package com.example.persistence.inmemrepo

import com.example.domain.user.UserModel
import com.example.domain.user.UserRepo
import org.springframework.stereotype.Repository

@Repository
class UserRepoImpl : UserRepo {

    private val users = mutableListOf<UserModel>()

    override fun save(user: UserModel): UserModel {
        users.add(user)
        return user
    }

    override fun delete(user: UserModel): UserModel {
        users.remove(user)
        return user
    }

    override fun cleanup(): Boolean {
        users.clear()
        return true
    }

    override fun update(user: UserModel): UserModel? {
        val foundIdx = users.indexOfFirst { it.id == user.id }
        if (foundIdx == -1) {
            return null
        }

        users[foundIdx] = user
        return user
    }

    override fun findByEmail(email: String): UserModel? {
        return users.find { it.email == email }
    }

    override fun get(userId: Long): UserModel? {
        return users.find { it.id == userId }
    }
}
