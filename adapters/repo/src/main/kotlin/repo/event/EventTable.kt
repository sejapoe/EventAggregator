package repo.event

import domain.entity.event.Event
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import repo.organizer.OrganizerEntity
import repo.organizer.OrganizerTable
import repo.user.UserEntity
import repo.user.UserTable
import java.time.ZoneOffset

object EventTable : IntIdTable() {
    val name = varchar("name", 255)
    val description = text("description")
    val date = datetime("date")
    val organizer = reference("organizer", OrganizerTable)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class EventEntity(id: EntityID<Int>) : IntEntity(id) {
    var name by EventTable.name
    var description by EventTable.description
    var date by EventTable.date
    var organizer by OrganizerEntity referencedOn EventTable.organizer
    var createdAt by EventTable.createdAt
    var updatedAt by EventTable.updatedAt
    val subscribers by EventSubscriberEntity referrersOn EventSubscriberTable.event

    fun toEvent() = Event(
        id.value,
        name,
        description,
        date,
        organizer.toOrganizer(),
        subscribers.map { it.toUser() },
        createdAt.toInstant(ZoneOffset.UTC),
        updatedAt.toInstant(ZoneOffset.UTC),
    )

    companion object : IntEntityClass<EventEntity>(EventTable)
}

object EventSubscriberTable : IntIdTable() {
    val event = reference("event", EventTable, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)
}

class EventSubscriberEntity(id: EntityID<Int>) : IntEntity(id) {
    var event by EventEntity referencedOn EventSubscriberTable.event
    var user by UserEntity referencedOn EventSubscriberTable.user

    fun toUser() = user.toUser()

    companion object : IntEntityClass<EventSubscriberEntity>(EventSubscriberTable)
}