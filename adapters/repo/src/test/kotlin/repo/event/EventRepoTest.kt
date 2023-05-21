package repo.event

import domain.entity.event.Event
import domain.entity.organizer.Organizer
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import kotlinx.coroutines.runBlocking
import repo.DatabaseFactory
import repo.organizer.OrganizerRepoImpl
import repo.user.UserRepoImpl
import java.time.LocalDateTime
import kotlin.test.*

class EventRepoTest {
    @BeforeTest
    fun setUp() {
        DatabaseFactory.init("org.h2.Driver", "jdbc:h2:mem:", "test", drop = true, test = true)
    }

    @Test
    fun `Event tests`() {
        runBlocking {
            val repo = EventRepoImpl()
            val userRepo = UserRepoImpl()
            val organizerRepo = OrganizerRepoImpl()
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

            // create organizer
            val organizerId = organizerRepo.save(
                organizer
            ).id
            val organizer1 = organizerRepo.get(organizerId)
            assertNotNull(organizer1)

            // Create event
            val event = Event(
                name = "Test Event",
                description = "Test Description",
                date = LocalDateTime.now().plusDays(1),
                organizer = organizer1
            )
            val eventId = repo.save(
                event
            ).id

            // Get created event
            val actual = repo.get(eventId)
            assertNotNull(actual)
            // New event has new id
            assertNotEquals(event.id, eventId)
            // Other data should be equal
            assertEquals(event.name, actual.name)
            assertEquals(event.description, actual.description)
            assertEquals(event.date, actual.date)
            assertEquals(event.organizer, actual.organizer)

            // Get all created events
            assertEquals(1, repo.getAll().size)
            assertEquals(listOf(actual), repo.getAll())
        }
    }
}