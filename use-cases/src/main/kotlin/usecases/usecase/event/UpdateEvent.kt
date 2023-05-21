package usecases.usecase.event

import domain.entity.event.EventNotFoundException
import domain.entity.user.Authorities
import domain.repo.event.EventRepo
import usecases.dependency.Logger
import usecases.model.EventModel
import usecases.model.UpdateEventModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.reflect.typeOf

@Mutation
class UpdateEvent(logger: Logger, private val repo: EventRepo) :
    UsecaseA1<UpdateEventModel, EventModel>(
        typeOf<UpdateEventModel>(), typeOf<EventModel>(), logger
    ) {
    override val authorities = listOf(Authorities.ORGANIZER)
    override val path = "/event/update"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, updateEvent: UpdateEventModel): EventModel {
        val event = repo.get(updateEvent.id) ?: throw EventNotFoundException()
        if (event.organizer.user.id != authentication!!.id) throw EventNotFoundException()
        return EventModel(
            repo.save(
                event.copy(
                    name = updateEvent.name,
                    description = updateEvent.description,
                    date = LocalDateTime.ofInstant(updateEvent.date, ZoneOffset.UTC),
                )
            )
        )
    }
}