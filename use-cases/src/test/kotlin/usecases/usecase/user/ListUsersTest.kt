package usecases.usecase.user

import domain.entity.user.AuthException
import domain.entity.user.LoginException
import domain.repo.user.UserRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.usecase.UsecaseTests
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("unused")
class ListUsersTest : UsecaseTests {
    private val repo = mockk<UserRepo>()
    override val usecase = ListUsers(logger, repo)

    override fun success() {
        runBlocking {
            every { runBlocking { repo.getAll() } } returns listOf(user)
            val result = usecase(userModel).items
            assertEquals(result, listOf(userModel))
        }
    }

    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null)
            }
        }
    }

    override fun `No user rules`() {
        runBlocking {
            assertFailsWith<AuthException> {
                usecase(userModel.copy(authorities = emptyList()))
            }
        }
    }
}