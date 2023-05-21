package usecases.model

import domain.entity.event.Event
import java.time.Instant
import java.time.LocalDateTime

data class EventModel(
    val id: Int,
    val name: String,
    val description: String,
    val organizer: OrganizerModel,
    val date: LocalDateTime,
) {
    constructor(event: Event) : this(
        id = event.id,
        name = event.name,
        description = event.description,
        organizer = OrganizerModel(event.organizer),
        date = event.date,
    )
}

data class NewEventModel(
    val name: String,
    val description: String,
    val organizerId: Int,
    val date: LocalDateTime,
)

data class UpdateEventModel(
    val id: Int,
    val name: String,
    val description: String,
    val organizerId: Int,
    val date: Instant,
)