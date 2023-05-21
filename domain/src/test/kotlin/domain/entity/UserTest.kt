package domain.entity

import domain.EmailInvalidException
import domain.PasswordInvalidException
import domain.entity.user.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UserTest {
    @Test
    fun `Valid User`() {
        User(
            id = -1,
            email = Email("test@test.com"),
            authorities = listOf(Authorities.USER),
            password = PasswordHash("test".reversed())
        )
    }

    @Test
    fun `Invalid Email`() {
        assertFailsWith<EmailInvalidException> {
            Email("test")
        }
    }

    @Test
    fun `Invalid Password`() {
        assertFailsWith<PasswordInvalidException> {
            NewPassword("test")
        }
    }
}