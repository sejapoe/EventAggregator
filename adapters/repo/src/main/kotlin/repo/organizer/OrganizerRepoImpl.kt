package repo.organizer

import domain.entity.organizer.Organizer
import domain.entity.user.UserNotFoundException
import domain.repo.organizer.OrganizerRepo
import repo.BaseDAO
import repo.user.UserEntity

class OrganizerRepoImpl : OrganizerRepo, BaseDAO<Organizer, Int, OrganizerEntity>(OrganizerEntity) {
    override fun OrganizerEntity.toDomain() = toOrganizer()
    override suspend fun create(entity: Organizer): Organizer {
        val organizerEntity = OrganizerEntity.new {
            name = entity.name
            description = entity.description
            user = UserEntity.findById(entity.user.id) ?: throw UserNotFoundException()
        }
        return organizerEntity.toOrganizer()
    }

    override suspend fun update(existing: OrganizerEntity, entity: Organizer): Organizer {
        existing.name = entity.name
        existing.description = entity.description
        existing.user = UserEntity.findById(entity.user.id) ?: throw UserNotFoundException()
        return existing.toOrganizer()
    }
}