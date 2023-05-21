package domain

open class InvalidPropertyException(message: String? = null, base: String = "Invalid property") :
    BaseException(base, message)

open class AlreadyExistsException(message: String? = null, base: String = "Already exists") :
    BaseException(base, message)

open class NotFoundException(message: String? = null, base: String = "Not found") : BaseException(base, message)

open class BaseException(base: String, message: String?) : Exception(message(base, message))

private fun message(base: String, message: String?) = base + if (message != null) ": $message" else ""