package config

import auth.JWTAuthenticatorImpl
import auth.PasswordEncoderImpl
import log.LoggerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import usecases.dependency.Authenticator
import usecases.dependency.Logger
import usecases.dependency.PasswordEncoder

fun modules(config: Config) = listOf(
    commonModule(config),
    userModule()
)

private fun commonModule(config: Config) = module {
    singleOf(::LoggerImpl).bind<Logger>()
    single<Authenticator> {
        JWTAuthenticatorImpl(
            issuer = config.jwt.domain,
            secret = config.jwt.secret,
        )
    }
    singleOf(::PasswordEncoderImpl).bind<PasswordEncoder>()
}

private fun userModule() = module {
    usecasesAndRepos("user")
}