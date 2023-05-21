package repo.utils

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> query(
    block: suspend () -> T
): T = newSuspendedTransaction { block() }