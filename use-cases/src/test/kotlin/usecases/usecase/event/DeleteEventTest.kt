package usecases.usecase.event

import domain.entity.user.AuthException
import domain.entity.user.LoginException
import domain.repo.event.EventRepo
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.usecase.UsecaseTest
import usecases.usecase.user.userModel
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DeleteEventTest : UsecaseTest {
    private val repo = mockk<EventRepo>()
    override val usecase = DeleteEvent(logger, repo)

    override fun success() {
        TODO("Not yet implemented")
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, idModel)
            }
        }
    }

    @Test
    override fun `No user rules`() {
        runBlocking {
            assertFailsWith<AuthException> {
                usecase(userModel.copy(authorities = emptyList()), idModel)
            }
        }
    }
}