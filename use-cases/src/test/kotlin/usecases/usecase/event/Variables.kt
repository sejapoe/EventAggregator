package usecases.usecase.event

import domain.entity.event.Event
import usecases.model.EventModel
import usecases.model.IdModel
import usecases.usecase.organizer.organizer
import java.time.LocalDateTime

val idModel = IdModel(1)
val event = Event(
    id = 1,
    name = "test",
    description = "test",
    organizer = organizer,
    date = LocalDateTime.now().plusDays(20)
)
val eventModel = EventModel(event)