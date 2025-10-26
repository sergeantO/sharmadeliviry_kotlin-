package com.example.domain

interface TransactionManager {
    suspend fun <T> inTransaction(block: suspend () -> T): T
    suspend fun inUnitTransaction(block: suspend () -> Unit): Unit
}
