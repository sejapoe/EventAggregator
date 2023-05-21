package log

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class LoggerImpl : usecases.dependency.Logger {
    private val loggers = mutableMapOf<String, Logger>()

    private val logger: Logger
        get() = Thread.currentThread().stackTrace[3].className.split(".").first().uppercase().let { module ->
            loggers.getOrPut(module) { LoggerFactory.getLogger(module) }
        }

    override fun info(message: String) {
        logger.info(message)
    }
}