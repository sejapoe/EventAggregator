package usecases.usecase.organizer

import domain.entity.user.Authorities
import domain.repo.organizer.OrganizerRepo
import usecases.dependency.Logger
import usecases.model.ListModel
import usecases.model.OrganizerModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
class ListOrganizers(logger: Logger, private val repo: OrganizerRepo) :
    UsecaseA0<ListModel<OrganizerModel>>(typeOf<ListModel<OrganizerModel>>(), logger) {
    override val authorities = listOf(Authorities.USER)
    override val path = "/organizer/list"

    override suspend fun executor(authentication: UserModel?) =
        ListModel(repo.getAll().map { usecases.model.OrganizerModel(it) })
}