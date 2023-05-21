package usecases.usecase.organizer

import domain.entity.user.AuthException
import domain.entity.user.LoginException
import domain.repo.organizer.OrganizerRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import usecases.logger
import usecases.model.UpdateOrganizerModel
import usecases.usecase.UsecaseTest
import usecases.usecase.user.userModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateOrganizerTest : UsecaseTest {
    private val repo = mockk<OrganizerRepo>()
    override val usecase = UpdateOrganizer(logger, repo)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { repo.get(id) } } returns organizer
            every { runBlocking { repo.save(any()) } } returns organizer
            val result = usecase(organizerUserModel, UpdateOrganizerModel(id, name, description))
            assertEquals(organizerModel, result)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, UpdateOrganizerModel(id, name, description))
            }
        }
    }

    @Test
    override fun `No user rules`() {
        runBlocking {
            assertFailsWith<AuthException> {
                usecase(userModel, UpdateOrganizerModel(id, name, description))
            }
        }
    }
}