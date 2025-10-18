package com.example.sharmadeliviry.domain.user

data class UserModel(
        val id: Long,
        val username: String,
        val email: String,
        val password: String,
        val address: String = "",
        val tgName: String = "",
)
