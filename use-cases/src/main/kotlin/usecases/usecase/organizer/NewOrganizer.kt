package usecases.usecase.organizer

import domain.entity.organizer.Organizer
import domain.entity.user.Authorities
import domain.entity.user.UserNotFoundException
import domain.repo.organizer.OrganizerRepo
import domain.repo.user.UserRepo
import usecases.dependency.Logger
import usecases.model.NewOrganizerModel
import usecases.model.OrganizerModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class NewOrganizer(logger: Logger, private val repo: OrganizerRepo, private val userRepo: UserRepo) :
    UsecaseA1<NewOrganizerModel, OrganizerModel>(typeOf<NewOrganizerModel>(), typeOf<OrganizerModel>(), logger) {

    override val path = "/organizer/new"

    override val authorities = listOf(Authorities.ORGANIZER)

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, newOrganizer: NewOrganizerModel): OrganizerModel {
        val organizer = repo.save(
            Organizer(
                name = newOrganizer.name,
                description = newOrganizer.description,
                user = userRepo.get(authentication!!.id) ?: throw UserNotFoundException()
            )
        )
        return OrganizerModel(organizer)
    }
}