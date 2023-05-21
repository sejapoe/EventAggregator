package usecases.usecase.event

import domain.entity.event.Event
import domain.entity.organizer.OrganizerNotFoundException
import domain.entity.user.Authorities
import domain.repo.event.EventRepo
import domain.repo.organizer.OrganizerRepo
import usecases.dependency.Logger
import usecases.model.EventModel
import usecases.model.NewEventModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class NewEvent(
    logger: Logger,
    private val repo: EventRepo,
    private val organizerRepo: OrganizerRepo
) :
    UsecaseA1<NewEventModel, EventModel>(typeOf<NewEventModel>(), typeOf<EventModel>(), logger) {
    override val authorities = listOf(Authorities.ORGANIZER)
    override val path = "/event/new"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, newEvent: NewEventModel): EventModel {
        val organizer = organizerRepo.get(newEvent.organizerId) ?: throw OrganizerNotFoundException()
        if (organizer.user.id != authentication!!.id) throw OrganizerNotFoundException()
        return EventModel(
            repo.save(
                Event(
                    name = newEvent.name,
                    description = newEvent.description,
                    organizer = organizer,
                    date = newEvent.date,
                )
            )
        )
    }
}