package com.example.sharmadeliviry

import com.example.domain.user.*
import com.example.persistence.inmemrepo.UserRepoImpl
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class UserInterractorTests {

    lateinit var repo: UserRepo
    lateinit var interractor: UserInterractor

    @BeforeAll
    fun setup() {
        repo = UserRepoImpl()
        interractor = UserInterractor(repo)
    }

    @BeforeEach
    fun repoCleanup() {
        repo.cleanup()
    }

    fun createTestUser(
            id: Long = 1L,
            email: String = "test@test.com",
            name: String = "test",
            password: String = "123"
    ): UserModel {
        return UserModel(id = id, email = email, username = name, password = password)
    }

    @Test
    fun `findUserByEmail should return null in unexisted user`() {
        // when
        val result = interractor.findUserByEmail("test@test.com")

        // then
        Assertions.assertNull(result)
    }

    @Test
    fun `findUserByEmail should return user in existed user`() {
        // when
        val testUser = createTestUser()
        repo.save(testUser)

        // when
        val result = interractor.findUserByEmail("test@test.com")

        // then
        Assertions.assertNotNull(result)
        Assertions.assertEquals(1L, testUser.id)
        Assertions.assertEquals("test", testUser.username)
    }

    @Test
    fun `createUser should add user to repo`() {
        // when
        val testUser = createTestUser()

        // when
        interractor.createUser(testUser)

        // then
        val result = repo.findByEmail("test@test.com")
        Assertions.assertNotNull(result)
    }

    @Test
    fun `updateUser should update only changed fields on existed user`() {
        // when
        val testUser1 = createTestUser()
        repo.save(testUser1)

        val testUser2 = createTestUser(name = "qwerty")

        // when
        val actual = interractor.updateUser(testUser2)

        // then
        Assertions.assertEquals(testUser2, actual)
        Assertions.assertEquals(testUser1.id, actual?.id)
        Assertions.assertEquals(testUser1.email, actual?.email)
    }

    @Test
    fun `updateUser should return null on unexisted user`() {
        // when
        val testUser2 = createTestUser(name = "qwerty")

        // when
        val actual = interractor.updateUser(testUser2)

        // then
        Assertions.assertNull(actual)
        // Assertions.assertThrows(expectedType, executable)
    }
}
