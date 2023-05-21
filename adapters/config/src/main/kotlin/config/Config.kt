package config

data class Config(
    val jwt: JWTConfig,
    val development: Boolean,
    val database: Database,
)

data class Database(
    val type: DatabaseType,
    val drop: Boolean,
    val jdbc: JDBC?,
)

data class JDBC(
    val driver: String,
    val url: String,
    val schema: String,
    val username: String,
    val password: String
)

enum class DatabaseType {
    LOCAL, JDBC
}

data class JWTConfig(
    val domain: String,
    val secret: String,
)