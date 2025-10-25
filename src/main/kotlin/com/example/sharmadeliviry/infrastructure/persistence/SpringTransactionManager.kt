package com.example.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate

import com.example.domain.TransactionManager

@Repository
class SpringTransactionManager(
    private val transactionTemplate: TransactionTemplate,
) : TransactionManager {
    override suspend fun <T> inTransaction(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            transactionTemplate.execute { _ -> runBlocking { block() } }!!
        }
}
