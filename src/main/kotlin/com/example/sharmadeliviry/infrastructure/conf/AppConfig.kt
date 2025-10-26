package com.example.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.support.TransactionTemplate

import com.example.application.user.UserReadRepo
import com.example.application.user.command.CreateUserCommand
import com.example.application.user.command.DeleteUserCommand
import com.example.application.user.command.UpdateUserCommand
import com.example.application.user.query.GetUserListQuery
import com.example.application.user.query.GetUserQuery
import com.example.domain.TransactionManager
import com.example.domain.user.UserWriteRepo

@Configuration
class AppConfig {

    @Bean fun getUserQuery(userReadRepo: UserReadRepo) = GetUserQuery(userReadRepo)

    @Bean fun getUserListQuery(userReadRepo: UserReadRepo) = GetUserListQuery(userReadRepo)

    @Bean fun createUserCommand(
        tm: TransactionManager,
        repo: UserWriteRepo,
    ) = CreateUserCommand(tm, repo)

    @Bean fun updateUserCommand(
        tm: TransactionManager,
        repo: UserWriteRepo,
    ) = UpdateUserCommand(tm, repo)

    @Bean fun deleteUserCommand(
        tm: TransactionManager,
        repo: UserWriteRepo,
    ) = DeleteUserCommand(tm, repo)
}
