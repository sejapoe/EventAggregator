package repo

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import repo.user.AuthorityTable
import repo.user.UserTable

object DatabaseFactory {
    private val tables = arrayOf(
        UserTable,
        AuthorityTable,
    )

    fun init(
        driver: String,
        url: String,
        schema: String,
        username: String = "",
        password: String = "",
        drop: Boolean = false,
        test: Boolean = false
    ) {
        // exposed
        val config = HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url + schema
            this.username = username
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val ds = HikariDataSource(config)
        Database.connect(ds)

        if (drop) {
            transaction {
                SchemaUtils.drop(*tables)
                SchemaUtils.drop(Table("flyway_schema_history"))
            }
        }

        val flyway = if (!test) {
            Flyway.configure()
                .baselineOnMigrate(true)
                .locations("classpath:repo/db/migration")
                .dataSource(ds)
                .load()
        } else null

        // If database is empty, first create missing tables and columns, then baseline
        // Else apply migrations before renamed tables and columns are created as new instead of being renamed
        if (flyway?.info()?.current() != null) {
            flyway.migrate()
        }

        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables)
        }

        if (flyway?.info()?.current() == null) {
            flyway?.migrate()
        }
    }
}