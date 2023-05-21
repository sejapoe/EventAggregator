package repo.organizer

import domain.entity.organizer.Organizer
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import repo.event.EventEntity
import repo.event.EventTable
import repo.user.UserEntity
import repo.user.UserTable
import java.time.ZoneOffset

object OrganizerTable : IntIdTable() {
    val user = reference("user", UserTable)
    val name = varchar("name", 255)
    val description = text("description")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class OrganizerEntity(id: EntityID<Int>) : IntEntity(id) {
    var user by UserEntity referencedOn OrganizerTable.user
    var name by OrganizerTable.name
    var description by OrganizerTable.description
    var createdAt by OrganizerTable.createdAt
    var updatedAt by OrganizerTable.updatedAt
    val events by EventEntity referrersOn EventTable.organizer

    fun toOrganizer() = Organizer(
        id.value,
        user.toUser(),
        name,
        description,
        createdAt.toInstant(ZoneOffset.UTC),
        updatedAt.toInstant(ZoneOffset.UTC),
    )

    companion object : IntEntityClass<OrganizerEntity>(OrganizerTable)
}