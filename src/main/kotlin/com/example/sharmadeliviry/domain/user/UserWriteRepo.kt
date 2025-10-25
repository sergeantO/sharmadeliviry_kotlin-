package com.example.domain.user

import com.example.domain.TransactionManager

interface UserWriteRepo {
    fun get(
        tm: TransactionManager,
        userId: UserId,
    ): Result<UserModel?>

    fun create(
        tm: TransactionManager,
        user: CreateUserModel,
    ): Result<UserModel>

    fun update(
        tm: TransactionManager,
        id: UserId,
        user: UpdateUserModel,
    ): Result<UserModel?>

    fun delete(
        tm: TransactionManager,
        id: UserId,
    ): Result<Boolean>

    fun cleanup(tm: TransactionManager): Result<Boolean>
}
