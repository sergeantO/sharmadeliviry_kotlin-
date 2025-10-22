package com.example.persistence

import com.example.application.port.TransactionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.support.TransactionTemplate

@Repository
class SpringTransactionManager(private val transactionTemplate: TransactionTemplate) :
        TransactionManager {

    override suspend fun <T> inTransaction(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            transactionTemplate.execute { _ -> runBlocking { block() } }!!
        }
    }
}
