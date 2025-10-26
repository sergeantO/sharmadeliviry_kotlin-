package com.example.persistence

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

import com.example.domain.TransactionManager
import org.jooq.DSLContext
import org.jooq.impl.DSL

@Component
class JooqTransactionManager : TransactionManager {
    override suspend fun <T> inTransaction(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                DSL.startTransaction()
                val result = block()
                DSL.commit()
                result
            } catch (e: Exception) {
                DSL.rollback()
                throw e
            }
        }

    override suspend fun inUnitTransaction(block: suspend () -> Unit): Unit =
        withContext(Dispatchers.IO) {
            try {
                DSL.startTransaction()
                block()
                DSL.commit()
            } catch (e: Exception) {
                DSL.rollback()
                throw e
            }
        }
}
