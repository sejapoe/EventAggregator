@file:Suppress("UNUSED_VARIABLE")
package domain.entity

import domain.entity.event.DateInvalidException
import domain.entity.event.Event
import domain.entity.event.EventInvalidException
import domain.entity.organizer.Organizer
import domain.entity.organizer.OrganizerInvalidException
import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith
class EventTests {
    private val validUser = User(
        email = Email("test@test.com"),
        authorities = listOf(Authorities.ORGANIZER),
        password = PasswordHash("test".reversed())
    )
    private val validOrganizer = Organizer(
        name = "Test Organizer",
        description = "Test Description",
        user = validUser
    )
    private val validEvent = Event(
        id = -1,
        name = "Test Event",
        description = "Test Description",
        date = LocalDateTime.now().plusDays(1),
        organizer = validOrganizer
    )

    @Test
    fun `Valid Event`() {
        Event(
            id = -1,
            name = "Test Event",
            description = "Test Description",
            date = LocalDateTime.now().plusDays(1),
            organizer = validOrganizer
        )
    }

    @Test
    fun `Invalid Organizer Authorities`() {
        assertFailsWith<OrganizerInvalidException> {
            val invalidOrganizer = validOrganizer.copy(
                user = User(
                    id = -1,
                    email = Email("test@test.com"),
                    authorities = listOf(Authorities.USER),
                    password = PasswordHash("test".reversed())
                )
            )
        }
    }

    @Test
    fun `Invalid Organizer Name`() {
        assertFailsWith<OrganizerInvalidException> {
            val invalidOrganizer = validOrganizer.copy(
                name = ""
            )
        }
    }

    @Test
    fun `Invalid Event Name`() {
        assertFailsWith<EventInvalidException> {
            val invalidEvent = validEvent.copy(
                name = "",
            )
        }
    }

    @Test
    fun `Invalid Event Date`() {
        assertFailsWith<DateInvalidException> {
            val invalidEvent = validEvent.copy(
                date = LocalDateTime.now().minusDays(1),
            )
        }
    }
}