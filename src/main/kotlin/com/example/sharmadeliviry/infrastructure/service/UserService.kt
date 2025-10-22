package com.example.presentation.service

import com.example.domain.user.UserInterractor
import com.example.domain.user.UserRepo
import org.springframework.stereotype.Service

@Service class UserService(private val repo: UserRepo) : UserInterractor(repo) {}
