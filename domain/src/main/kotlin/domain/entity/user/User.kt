package domain.entity.user

import domain.EmailInvalidException
import domain.PasswordInvalidException
import domain.entity.Entity
import domain.entity.ValueClass

data class User(
    override val id: Int = -1,
    val email: Email,
    val authorities: List<Authorities> = listOf(),
    val password: PasswordHash,
) : Entity

enum class Authorities {
    USER, MANAGER, ADMIN
}

data class Email(
    override val value: String
) : ValueClass<String> {
    init {
        if (!value.contains('@')) throw EmailInvalidException()
    }
}

data class PasswordHash(
    override val value: String
) : ValueClass<String> {
    override fun toString(): String {
        return "PasswordHash()"
    }
}

open class Password(
    override val value: String
) : ValueClass<String> {
    override fun toString(): String {
        return "Password()"
    }
}

class NewPassword(value: String) : Password(value) {
    init {
        if (!value.matches(Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")))
            throw PasswordInvalidException()
    }

    override fun toString(): String {
        return "NewPassword()"
    }
}