package usecases.usecase.organizer

import domain.entity.organizer.OrganizerNotFoundException
import domain.entity.user.Authorities
import domain.repo.organizer.OrganizerRepo
import usecases.dependency.Logger
import usecases.model.OrganizerModel
import usecases.model.UpdateOrganizerModel
import usecases.model.UserModel
import usecases.usecase.Mutation
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Mutation
class UpdateOrganizer(logger: Logger, private val repo: OrganizerRepo) :
    UsecaseA1<UpdateOrganizerModel, OrganizerModel>(typeOf<UpdateOrganizerModel>(), typeOf<OrganizerModel>(), logger) {
    override val authorities = listOf(Authorities.ORGANIZER)
    override val path = "/organizer/update"

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, updateOrganizer: UpdateOrganizerModel): OrganizerModel {
        val organizer = repo.get(updateOrganizer.id) ?: throw OrganizerNotFoundException()
        if (organizer.user.id != authentication!!.id) throw OrganizerNotFoundException()
        return OrganizerModel(
            repo.save(
                organizer.copy(
                    name = updateOrganizer.name,
                    description = updateOrganizer.description,
                )
            )
        )
    }
}