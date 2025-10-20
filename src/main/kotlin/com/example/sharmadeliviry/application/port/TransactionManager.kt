package com.example.application.port

interface TransactionManager {
    suspend fun <T> inTransaction(block: suspend () -> T): T
}
