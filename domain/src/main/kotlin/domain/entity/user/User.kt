package domain.entity.user

import domain.entity.Entity
import domain.entity.ValueClass

data class User(
    override val id: Int,
    val email: Email,
    val authorities: List<Authorities> = listOf(),
    val password: PasswordHash,
) : Entity

enum class Authorities {
    USER, ADMIN
}

data class Email(
    override val value: String
) : ValueClass<String>

data class PasswordHash(
    override val value: String
) : ValueClass<String>

data class Password(
    override val value: String
) : ValueClass<String>