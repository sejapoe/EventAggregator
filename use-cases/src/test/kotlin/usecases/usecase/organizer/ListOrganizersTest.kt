package usecases.usecase.organizer

import domain.entity.user.AuthException
import domain.entity.user.LoginException
import domain.repo.organizer.OrganizerRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.usecase.UsecaseTest
import usecases.usecase.user.userModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListOrganizersTest : UsecaseTest {
    val repo = mockk<OrganizerRepo>()
    override val usecase = ListOrganizers(logger, repo)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repo.getAll() } } returns listOf(organizer)
            val result = usecase(userModel).items
            assertEquals(listOf(organizerModel), result)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null)
            }
        }
    }

    @Test
    override fun `No user rules`() {
        runBlocking {
            assertFailsWith<AuthException> {
                usecase(userModel.copy(authorities = emptyList()))
            }
        }
    }
}