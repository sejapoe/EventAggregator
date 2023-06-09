package usecases.usecase

import domain.entity.user.AuthException
import domain.entity.user.Authorities
import domain.entity.user.LoginException
import usecases.dependency.Logger
import usecases.model.UserModel
import kotlin.reflect.KType

@Target(AnnotationTarget.CLASS)
annotation class Query

@Target(AnnotationTarget.CLASS)
annotation class Mutation

sealed class UsecaseType<R : Any>(
    val result: KType,
    private val logger: Logger
) {
    abstract val authorities: List<Authorities>
    abstract val args: List<KType>
    open val authenticated = true
    open val logging = true
    open val path: String = this::class.simpleName!!.lowercase()
    protected fun before(authentication: UserModel?, vararg args: Any): UserModel? {
        authorize(authentication)
        if (logging) {
            logger.info(
                "${this::class.simpleName!!} * ${authentication?.email?.value ?: "Unauthenticated"}: ${
                    args.joinToString(" | ")
                }"
            )
        }
        return authentication
    }

    private fun authorize(authentication: UserModel?) {
        if (authenticated) {
            if (authentication == null) throw LoginException()
            if (authorities.isNotEmpty() && !authorities.any { it in authentication.authorities }) {
                throw AuthException()
            }
        }
    }
}

abstract class UsecaseA0<R : Any>(
    result: KType,
    logger: Logger
) : UsecaseType<R>(result, logger), suspend (UserModel?) -> R {

    final override val args get() = emptyList<KType>()
    protected abstract suspend fun executor(authentication: UserModel?): R
    override suspend fun invoke(authentication: UserModel?) = executor(before(authentication))
}

abstract class UsecaseA1<A0 : Any, R : Any>(
    private val a0: KType,
    result: KType,
    logger: Logger
) : UsecaseType<R>(result, logger), suspend (UserModel?, A0) -> R {

    final override val args get() = listOf(a0)
    protected abstract suspend fun executor(authentication: UserModel?, a0: A0): R
    override suspend fun invoke(authentication: UserModel?, a0: A0) = executor(before(authentication, a0), a0)
}
