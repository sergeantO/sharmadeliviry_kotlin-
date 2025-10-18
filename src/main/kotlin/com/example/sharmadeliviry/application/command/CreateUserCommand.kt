package com.example.sharmadeliviry.application.command

import com.example.sharmadeliviry.application.port.TransactionManager
import com.example.sharmadeliviry.domain.user.UserModel
import kotlin.random.Random

class CreateUserCommand {

        suspend fun execute(
                dto: CreateUserRequest,
                _transactionManager: TransactionManager
        ): CreateUserResponse {
                val user = dto.toUserModel()
                // TODO: release some buisnes logic
                return user.toCreateUserResponse()
        }
}

data class CreateUserRequest(
        val name: String,
        val email: String,
        val password: String,
        val address: String = "",
        val tgName: String = "",
)

private fun CreateUserRequest.toUserModel(): UserModel {
        val randomLong = Random.nextLong()
        return UserModel(id = randomLong, username = name, email = email, password = password)
}

data class CreateUserResponse(
        val id: Long,
        val name: String,
        val email: String,
        val address: String = "",
        val tgName: String = "",
)

private fun UserModel.toCreateUserResponse(): CreateUserResponse {
        return CreateUserResponse(
                id = id,
                name = username,
                email = email,
                address = address,
                tgName = tgName,
        )
}
