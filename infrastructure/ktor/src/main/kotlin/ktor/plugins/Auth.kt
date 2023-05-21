package ktor.plugins

import auth.JWTAuthenticatorImpl
import config.Config
import domain.entity.user.Email
import domain.entity.user.Password
import domain.repo.user.UserRepo
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.date.*
import usecases.dependency.Authenticator
import usecases.model.LoginUserModel
import usecases.model.UserModel
import usecases.usecase.user.LoginUser

private const val AUTH_COOKIE = "JWT"

class UserPrincipal(
    val user: UserModel
) : Principal

fun Application.auth(config: Config) {
    install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    install(Authentication) {
        jwt {
            authHeader { call ->
                call.request.cookies[AUTH_COOKIE]?.let { parseAuthorizationHeader(it)  }
                    ?: call.request.parseAuthorizationHeader()
            }
            val authenticator = this@auth.get<Authenticator>() as JWTAuthenticatorImpl
            val userRepo = this@auth.get<UserRepo>()
            verifier {
                authenticator.verifier
            }
//            verifier(authenticator.verifier)
            validate { jwtCredential ->
                userRepo.get(jwtCredential.payload.subject.toInt())?.let {
                    UserPrincipal(UserModel(it))
                }
            }
        }
    }

    routing {
        install(ContentNegotiation) {
            gson()
        }

        post("/login") {
            try {
                val login = call.receive<Map<String, String>>()
                    .let { LoginUserModel(Email(it["email"]!!), Password(it["password"]!!)) }
                val loginUser = this@routing.get<LoginUser>()
                call.response.cookies.append(
                    Cookie(
                        AUTH_COOKIE,
                        loginUser(null, login),
                        httpOnly = true,
                        secure = !config.development,
                        extensions = mapOf("SameSite" to "lax")
                    )
                )
                call.respond(HttpStatusCode.OK)
            } catch (ex: Exception) {
                ex.printStackTrace()
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/logout") {
            call.response.cookies.append(AUTH_COOKIE, "", expires = GMTDate.START)
            call.respond(HttpStatusCode.OK)
        }
    }
}