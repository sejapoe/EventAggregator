package usecases.usecase.user

import domain.entity.user.Authorities
import domain.repo.user.UserRepo
import usecases.dependency.Logger
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA0
import kotlin.reflect.typeOf

data class ListUsersModel(
    val items: List<UserModel>,
    val total: Long
)

@Query
@Suppress("unused")
class ListUsers(logger: Logger, private val repo: UserRepo) :
    UsecaseA0<ListUsersModel>(typeOf<ListUsersModel>(), logger) {
//    override val authenticated = false
    override val authorities: List<Authorities>
        get() = listOf(Authorities.USER)

    override suspend fun executor(authentication: UserModel?): ListUsersModel {
        val items = repo.getAll().map { UserModel(it) }
        return ListUsersModel(items, items.size.toLong())
    }
}