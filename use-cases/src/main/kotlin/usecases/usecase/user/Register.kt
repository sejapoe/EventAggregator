package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.PasswordHash
import domain.entity.user.User
import domain.entity.user.UserAlreadyExistsException
import domain.repo.user.UserRepo
import usecases.dependency.Logger
import usecases.model.RegisterUserModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class Register(logger: Logger, private val repo: UserRepo) :
    UsecaseA1<RegisterUserModel, UserModel>(typeOf<RegisterUserModel>(), typeOf<UserModel>(), logger) {
    override val authenticated = false
    override val authorities = emptyList<Authorities>()

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, register: RegisterUserModel): UserModel {
        if (repo.findByEmail(register.email) != null) throw UserAlreadyExistsException()
        val user = User(
            email = register.email,
            password = PasswordHash(register.password.value),
            authorities = if (register.isManager) listOf(
                Authorities.MANAGER,
                Authorities.USER
            ) else listOf(Authorities.USER),
        )
        val saved = repo.save(user)
        return UserModel(saved)
    }
}