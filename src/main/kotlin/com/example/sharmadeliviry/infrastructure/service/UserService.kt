package com.example.sharmadeliviry.presentation.service

import com.example.sharmadeliviry.domain.user.UserInterractor
import com.example.sharmadeliviry.domain.user.UserRepo
import org.springframework.stereotype.Service

@Service class UserService(private val repo: UserRepo) : UserInterractor(repo) {}
