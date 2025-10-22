package com.example.presentation.controller

import com.example.application.command.CreateUserCommand
import com.example.application.command.CreateUserRequest
import com.example.application.port.TransactionManager
import com.example.application.query.GetUserQuery
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
        private val transactionManager: TransactionManager,
        private val createUserCommand: CreateUserCommand,
        private val getUserQuery: GetUserQuery
) {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/:id")
    suspend fun get(@PathVariable("id") id: Long) {
        getUserQuery.execute(id)
    }

    @PostMapping("/register")
    suspend fun register(@RequestBody userDto: CreateUserRequest) {
        createUserCommand.execute(userDto, transactionManager)
    }
}
