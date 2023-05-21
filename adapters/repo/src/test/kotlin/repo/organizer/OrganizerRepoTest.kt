package repo.organizer

import domain.entity.organizer.Organizer
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import kotlinx.coroutines.runBlocking
import repo.DatabaseFactory
import repo.user.UserRepoImpl
import kotlin.test.*

class OrganizerRepoTest {
    @BeforeTest
    fun setUp() {
        DatabaseFactory.init("org.h2.Driver", "jdbc:h2:mem:", "test", drop = true, test = true)
    }

    @Test
    fun `Organizer tests`() {
        runBlocking {
            val repo = OrganizerRepoImpl()
            val userRepo = UserRepoImpl()
            // initialize user and organizer
            val user = userRepo.save(
                User(
                    email = Email("test@test.com"),
                    authorities = listOf(Authorities.ORGANIZER),
                    password = PasswordHash("test".reversed())
                )
            )
            val organizer = Organizer(
                name = "Test Organizer",
                description = "Test Description",
                user = user
            )

            // Event does not exist
            assertNull(repo.get(1))
            assertEquals(emptyList(), repo.getAll())

            // create organizer
            val organizerId = repo.save(
                organizer
            ).id
            val actual = repo.get(organizerId)
            assertNotNull(actual)
            // new id
            assertNotEquals(organizer.id, actual.id)
            // other data should be equal
            assertEquals(organizer.name, actual.name)
            assertEquals(organizer.description, actual.description)
            assertEquals(organizer.user, actual.user)

            // get all organizers
            assertEquals(listOf(actual), repo.getAll())
        }
    }
}