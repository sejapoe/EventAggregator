package usecases

import usecases.dependency.Logger

val logger = TestLogger()

class TestLogger : Logger {
    override fun info(message: String) {
        println(message)
    }
}