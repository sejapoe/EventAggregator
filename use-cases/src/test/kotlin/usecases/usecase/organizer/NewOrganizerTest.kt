package usecases.usecase.organizer

import domain.entity.user.AuthException
import domain.entity.user.LoginException
import domain.repo.organizer.OrganizerRepo
import domain.repo.user.UserRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import usecases.logger
import usecases.model.NewOrganizerModel
import usecases.usecase.UsecaseTest
import usecases.usecase.user.userModel
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NewOrganizerTest : UsecaseTest {
    val repo = mockk<OrganizerRepo>()
    val userRepo = mockk<UserRepo>()
    override val usecase = NewOrganizer(logger, repo, userRepo)

    @Test
    override fun success() {
        runBlocking {
            every { runBlocking { userRepo.get(id) } } returns organizerUser
            every { runBlocking { repo.save(any()) } } returns organizer
            val result = usecase(organizerUserModel, NewOrganizerModel(name, description))
            assertEquals(organizerModel, result)
        }
    }

    @Test
    override fun unauthenticated() {
        runBlocking {
            assertFailsWith<LoginException> {
                usecase(null, NewOrganizerModel(name, description))
            }
        }
    }

    @Test
    override fun `No user rules`() {
        runBlocking {
            assertFailsWith<AuthException> {
                usecase(userModel, NewOrganizerModel(name, description))
            }
        }
    }
}