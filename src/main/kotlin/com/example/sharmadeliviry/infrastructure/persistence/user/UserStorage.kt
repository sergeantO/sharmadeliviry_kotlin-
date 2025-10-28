package com.example.infrastructure.persistence.user

import org.springframework.stereotype.Component

import com.example.domain.user.UserModel

@Component
class UserStorage {
    val items = mutableListOf<UserModel>()
}
