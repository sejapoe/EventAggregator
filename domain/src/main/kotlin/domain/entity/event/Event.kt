package domain.entity.event

import domain.entity.Entity
import domain.entity.organizer.Organizer
import domain.entity.organizer.OrganizerFilter
import domain.entity.user.User
import java.time.Instant
import java.time.LocalDateTime

data class Event(
    override val id: Int = -1,
    val name: String,
    val description: String,
    val date: LocalDateTime,
    val organizer: Organizer,
    val subscribers: List<User> = listOf(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) : Entity<Int> {
    init {
        if (
            name.isBlank()
            || description.isBlank()
            || name.length > 255
        ) throw EventInvalidException()
        if (date.isBefore(LocalDateTime.now())) throw DateInvalidException()
    }
}

data class EventFilter(
    val name: String? = null,
    val description: String? = null,
    val after: LocalDateTime? = null,
    val before: LocalDateTime? = null,
    val organizer: OrganizerFilter? = null,
)