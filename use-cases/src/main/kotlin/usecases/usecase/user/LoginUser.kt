package usecases.usecase.user

import domain.entity.user.Authorities
import domain.entity.user.LoginException
import domain.repo.user.UserRepo
import usecases.dependency.Authenticator
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.Query
import usecases.usecase.UsecaseA1
import kotlin.reflect.typeOf

@Query
class LoginUser(
    logger: Logger,
    private val repo: UserRepo,
    private val authenticator: Authenticator,
    private val passwordEncoder: PasswordEncoder
) : UsecaseA1<LoginUserModel, String>(typeOf<LoginUserModel>(), typeOf<String>(), logger) {
    override val authenticated = false
    override val authorities = emptyList<Authorities>()

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun executor(authentication: UserModel?, login: LoginUserModel): String {
        val user = repo.findByEmail(login.email)
        if (user == null || !passwordEncoder.matches(login.password, user.password))
            throw LoginException()
        return "Bearer ${authenticator.generate(UserModel(user))}"
    }
}