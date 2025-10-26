package com.example.sharmadeliviry.infrastructure.persistence.user

import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.example.domain.TransactionManager
import com.example.domain.user.CreateUserModel
import com.example.domain.user.Email
import com.example.domain.user.UpdateUserModel
import com.example.infrastructure.persistence.user.UserReadImMemRepo
import com.example.infrastructure.persistence.user.UserStorage
import com.example.infrastructure.persistence.user.UserWriteImMemRepo

class UserRepositoryComponentTest {
    private lateinit var userStorage: UserStorage
    private lateinit var userWriteRepo: UserWriteImMemRepo
    private lateinit var userReadRepo: UserReadImMemRepo
    private lateinit var transactionManager: TransactionManager

    @BeforeEach
    fun setUp() {
        userStorage = UserStorage()
        userWriteRepo = UserWriteImMemRepo(userStorage)
        userReadRepo = UserReadImMemRepo(userStorage)
        transactionManager =
            object : TransactionManager {
                override suspend fun <T> inTransaction(block: suspend () -> T): T = runBlocking { block() }
            }
    }

    @Test
    fun `should create user and read it successfully`() =
        runBlocking {
            // Arrange
            val createUserModel =
                CreateUserModel(
                    username = "testuser",
                    email = Email("test@example.com"),
                    password = "password123",
                )

            // Act
            val createdUser = userWriteRepo.create(transactionManager, createUserModel).getOrNull()!!
            val readUser = userReadRepo.get(createdUser.id).getOrNull()
            val readUserByEmail = userReadRepo.findByEmail(createdUser.email).getOrNull()

            // Assert
            Assertions.assertNotNull(readUser)
            Assertions.assertEquals(createdUser, readUser)
            Assertions.assertEquals(createdUser, readUserByEmail)
        }

    @Test
    fun `should update user email and read updated user`() =
        runBlocking {
            // Arrange
            val createUserModel =
                CreateUserModel(
                    username = "testuser",
                    email = Email("test@example.com"),
                    password = "password123",
                )
            val createdUser = userWriteRepo.create(transactionManager, createUserModel).getOrNull()!!
            val newEmail = Email("updated@example.com")

            // Act
            val updatedUser =
                userWriteRepo
                    .update(
                        transactionManager,
                        createdUser.id,
                        UpdateUserModel(
                            email = newEmail,
                            username = null,
                            password = null,
                        ),
                    ).getOrNull()
            val readUpdatedUser = userReadRepo.get(createdUser.id).getOrNull()

            // Assert
            Assertions.assertNotNull(updatedUser)
            Assertions.assertEquals(newEmail, updatedUser?.email)
            Assertions.assertEquals(newEmail, readUpdatedUser?.email)
        }

    @Test
    fun `should delete user and verify removal`() =
        runBlocking {
            // Arrange
            val createUserModel =
                CreateUserModel(
                    username = "testuser",
                    email = Email("test@example.com"),
                    password = "password123",
                )
            val createdUser = userWriteRepo.create(transactionManager, createUserModel).getOrNull()!!

            // Act
            val deleteResult = userWriteRepo.delete(transactionManager, createdUser.id)
            val readDeletedUser = userReadRepo.get(createdUser.id).getOrNull()

            // Assert
            Assertions.assertTrue(deleteResult.isSuccess)
            Assertions.assertNull(readDeletedUser)
        }

    @Test
    fun `should cleanup all users`() =
        runBlocking {
            // Arrange
            val createUserModel1 =
                CreateUserModel(
                    username = "testuser1",
                    email = Email("test1@example.com"),
                    password = "password123",
                )
            val createUserModel2 =
                CreateUserModel(
                    username = "testuser2",
                    email = Email("test2@example.com"),
                    password = "password123",
                )
            userWriteRepo.create(transactionManager, createUserModel1)
            userWriteRepo.create(transactionManager, createUserModel2)

            // Act
            val cleanupResult = userWriteRepo.cleanup(transactionManager)
            val remainingUsers = userReadRepo.getAll().getOrNull()!!

            // Assert
            Assertions.assertTrue(cleanupResult.isSuccess)
            Assertions.assertTrue(remainingUsers.isEmpty())
        }
}
