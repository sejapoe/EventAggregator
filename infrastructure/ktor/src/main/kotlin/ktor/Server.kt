package ktor

import config.getAll
import config.modules
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.component.Components
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.security.BearerAuth
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktor.plugins.auth
import ktor.plugins.rest
import org.koin.ktor.plugin.Koin
import usecases.usecase.UsecaseType
import java.time.Instant
import java.util.*
import kotlin.reflect.typeOf

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()


    install(Koin) {
        modules(modules(config))
    }

    auth(config)

    val usecases = getAll<UsecaseType<*>>()

    install(NotarizedApplication()) {
        spec = OpenApiSpec(
            info = Info(
                title = "EventAggregator",
                version = "1.0.0"
            ),
            components = Components(
                securitySchemes = mutableMapOf(
                    "bearer" to BearerAuth()
                )
            )
        )
        customTypes = mapOf(
            typeOf<Instant>() to TypeDefinition(type = "string", format = "date-time"),
            typeOf<Date>() to TypeDefinition(type = "string", format = "date-time")
        )
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }

    rest(usecases)

//    launch {
//        setup(get(), get())
//    }
}