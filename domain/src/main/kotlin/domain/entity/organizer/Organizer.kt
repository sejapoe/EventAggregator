package domain.entity.organizer

import domain.entity.Entity
import domain.entity.user.Authorities
import domain.entity.user.User
import domain.entity.user.UserFilter
import java.time.Instant

data class OrganizerFilter(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val user: UserFilter? = null,
)


data class Organizer(
    override val id: Int = -1,
    val user: User,
    val name: String,
    val description: String,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
) : Entity<Int> {
    init {
        if (
            !user.authorities.contains(Authorities.ORGANIZER)
            || name.isBlank()
            || description.isBlank()
            || name.length > 255
        ) throw OrganizerInvalidException()
    }
}