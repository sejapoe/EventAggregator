package repo.event

import domain.entity.event.Event
import domain.entity.event.EventFilter
import domain.entity.organizer.OrganizerNotFoundException
import domain.repo.event.EventRepo
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import repo.BaseDAO
import repo.organizer.OrganizerEntity
import repo.organizer.OrganizerTable
import repo.user.UserTable
import repo.utils.*

class EventRepoImpl : EventRepo, BaseDAO<Event, Int, EventEntity>(EventEntity) {
    override fun EventEntity.toDomain() = toEvent()

    override suspend fun getAll(filter: EventFilter): List<Event> = query {

        val op = Op.build {
            var expression: Op<Boolean> = (EventTable.id greater 0) and
                    (EventTable.name likeInside filter.name) and
                    (EventTable.date less filter.before) and
                    (EventTable.date greater filter.after)
            if (filter.organizer != null) {
                val organizerFilter = filter.organizer!!
                var organizerExpression = (OrganizerTable.id eqn organizerFilter.id) and
                        (OrganizerTable.name likeInside organizerFilter.name) and
                        (OrganizerTable.description likeInside organizerFilter.description)
                if (organizerFilter.user != null) {
                    val userFilter = organizerFilter.user!!
                    val userExpression =
                        (UserTable.id eqn userFilter.id) and (UserTable.email likeInside userFilter.email)
                    organizerExpression =
                        organizerExpression and (OrganizerTable.user inSubQuery UserTable.select(userExpression))
                }
                expression = expression and (EventTable.organizer inSubQuery OrganizerTable.select(organizerExpression))
            }
            expression
        }
        EventEntity.find(op).map { it.toEvent() }
    }

    override suspend fun create(entity: Event): Event = query {
        val eventEntity = EventEntity.new {
            name = entity.name
            description = entity.description
            date = entity.date
            organizer = OrganizerEntity.findById(entity.organizer.id) ?: throw OrganizerNotFoundException()
        }
        eventEntity.toEvent()
    }

    override suspend fun update(existing: EventEntity, entity: Event): Event = query {
        existing.name = entity.name
        existing.description = entity.description
        existing.date = entity.date
        existing.organizer = OrganizerEntity.findById(entity.organizer.id) ?: throw OrganizerNotFoundException()
        existing.toEvent()
    }
}
