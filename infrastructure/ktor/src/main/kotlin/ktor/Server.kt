package ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun main(args: Array<String>) = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)

    val config = config()

//    val usecases = getAll<UsecaseType<*>>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}