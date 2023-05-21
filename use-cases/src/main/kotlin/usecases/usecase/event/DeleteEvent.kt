package usecases.usecase.event

import domain.entity.event.EventNotFoundException
import domain.entity.user.Authorities
import domain.repo.event.EventRepo
import usecases.dependency.Logger
import usecases.model.IdModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class DeleteEvent(logger: Logger, private val repo: EventRepo) :
    UsecaseA1<IdModel, Unit>(typeOf<IdModel>(), typeOf<Unit>(), logger) {
    override val authorities = listOf(Authorities.ORGANIZER)
    override val path = "/event/delete"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, delete: IdModel) {
        val event = repo.get(delete.id) ?: throw EventNotFoundException()
        if (event.organizer.user.id != authentication!!.id) throw EventNotFoundException()
        repo.delete(event.id)
    }
}