package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repo.user.UserRepo
import usecases.dependency.Logger
import usecases.model.ListModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

@Query
@Suppress("unused")
class ListUsers(logger: Logger, private val repo: UserRepo) :
    UsecaseA0<ListModel<UserModel>>(typeOf<ListModel<UserModel>>(), logger) {
    override val path = "/user/list"
    override val authorities: List<Authorities>
        get() = listOf(Authorities.USER)

    override suspend fun executor(authentication: UserModel?) =
        ListModel(repo.getAll().map { UserModel(it) })
}