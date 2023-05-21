package domain.repo.event

import domain.entity.event.Event
import domain.entity.event.EventFilter
import domain.repo.Repo

interface EventRepo : Repo<Event, Int> {
    suspend fun getAll(filter: EventFilter): List<Event>
}