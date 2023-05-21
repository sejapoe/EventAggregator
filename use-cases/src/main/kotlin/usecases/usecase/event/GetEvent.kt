package usecases.usecase.event

import domain.entity.event.EventNotFoundException
import domain.entity.user.Authorities
import domain.repo.event.EventRepo
import usecases.dependency.Logger
import usecases.model.EventModel
import usecases.model.IdModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class GetEvent(logger: Logger, private val repo: EventRepo) :
    UsecaseA1<IdModel, EventModel>(typeOf<IdModel>(), typeOf<EventModel>(), logger) {
    override val authorities = listOf(Authorities.USER)
    override val path = "/event/get"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, id: IdModel): EventModel {
        val event = repo.get(id.id) ?: throw EventNotFoundException()
        return EventModel(event)
    }
}