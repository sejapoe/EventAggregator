package usecases.usecase.event

import domain.entity.event.EventFilter
import domain.entity.user.Authorities
import domain.repo.event.EventRepo
import usecases.dependency.Logger
import usecases.model.EventModel
import usecases.model.ListModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class ListEvents(logger: Logger, private val eventRepo: EventRepo) :
    UsecaseA1<EventFilter, ListModel<EventModel>>(typeOf<EventFilter>(), typeOf<ListModel<EventModel>>(), logger) {
    override val authorities = listOf(Authorities.USER)
    override val path = "/event/list"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, filter: EventFilter) =
        ListModel(eventRepo.getAll(filter).map { EventModel(it) })
}