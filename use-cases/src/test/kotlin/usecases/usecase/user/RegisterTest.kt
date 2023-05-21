package usecases.usecase.user

import domain.EmailInvalidException
import domain.PasswordInvalidException
import domain.UserAlreadyExistsException
import domain.entity.user.Email
import domain.entity.user.NewPassword
import domain.repo.user.UserRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.model.RegisterUserModel
import usecases.usecase.UsecaseTests
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class RegisterTest : UsecaseTests {
    private val repo = mockk<UserRepo>()
    override val usecase = Register(logger, repo)

    private val registerModel = RegisterUserModel(email, NewPassword(password.value))

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repo.findByEmail(email) } } returns null
            every { runBlocking { repo.save(any()) } } returns user
            val result = usecase(null, registerModel)
            assertEquals(userModel, result)
        }
    }

    @Test
    fun `User already exists`() {
        runBlocking {
            every { runBlocking { repo.findByEmail(email) } } returns user
            assertFailsWith<UserAlreadyExistsException> {
                usecase(null, registerModel)
            }
        }
    }

    @Test
    fun `Invalid email`() {
        runBlocking {
            assertFailsWith<EmailInvalidException> {
                usecase(null, registerModel.copy(email = Email("invalid")))
            }
        }
    }

    @Test
    fun `Invalid password`() {
        runBlocking {
            assertFailsWith<PasswordInvalidException> {
                usecase(null, registerModel.copy(password = NewPassword("123")))
            }
        }
    }

    @Test
    override fun unauthenticated() = success()

    @Test
    override fun `No user rules`() = success()
}