package usecases.usecase.user

import domain.entity.user.LoginException
import domain.entity.user.Password
import domain.entity.user.PasswordHash
import domain.repo.user.UserRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.dependency.Authenticator
import usecases.dependency.PasswordEncoder
import usecases.logger
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.UsecaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginUserTest : UsecaseTest {
    private val repo = mockk<UserRepo>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val authenticator = mockk<Authenticator>()
    override val usecase = LoginUser(logger, repo, authenticator, passwordEncoder)

    private val loginUserModel = LoginUserModel(email, password)

    @Test
    override fun success() {
        runBlocking {
            val user = user.copy(authorities = emptyList())
            every { runBlocking { repo.findByEmail(email) } } returns user
            every { passwordEncoder.matches(password, PasswordHash(password.value.reversed())) } returns true
            every { authenticator.generate(UserModel(user)) } returns "token"
            val result = usecase(null, loginUserModel)
            assertEquals("Bearer token", result)
        }
    }

    @Test
    override fun unauthenticated() = success()

    @Test
    override fun `No user rules`() = success()

    @Test
    fun `Invalid email`() {
        runBlocking {
            assertFailsWith<LoginException> {
                every { runBlocking { repo.findByEmail(email) } } returns null
                usecase(null, loginUserModel)
            }
        }
    }

    @Test
    fun `Invalid password`() {
        runBlocking {
            assertFailsWith<LoginException> {
                val pass = Password(password.value + 1)
                every { runBlocking { repo.findByEmail(email) } } returns user
                every { passwordEncoder.matches(pass, PasswordHash(password.value.reversed())) } returns false
                usecase(null, loginUserModel.copy(password = pass))
            }
        }
    }
}