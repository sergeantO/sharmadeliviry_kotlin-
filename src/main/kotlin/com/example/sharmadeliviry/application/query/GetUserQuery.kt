package com.example.application.query

import com.example.domain.user.UserModel
import com.example.domain.user.UserRepo

class GetUserQuery {

    fun execute(userId: Long, userRepo: UserRepo): UserDto? {
        // TODO: некорректно переиспользовать одно репо для чтения и записи, требуется разделение
        return userRepo.get(userId)?.toUserDto()
    }
}

data class UserDto(
        val id: Long,
        val name: String,
        val email: String,
        val address: String = "",
        val tgName: String = "",
)

private fun UserModel.toUserDto(): UserDto {
    return UserDto(id = id, name = username, email = email, address = address, tgName = tgName)
}
