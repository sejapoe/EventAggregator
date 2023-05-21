package domain

open class InvalidPropertyException(message: String? = null, base: String = "Invalid property") :
    Exception(message(base, message))

class EmailInvalidException(message: String? = null, base: String = "Email invalid") :
    InvalidPropertyException(message, base)

class PasswordInvalidException(message: String? = null, base: String = "Password invalid") :
    InvalidPropertyException(message, base)

open class AlreadyExistsException(message: String? = null, base: String = "Already exists") :
    Exception(message(base, message))

class AuthException(message: String? = null, base: String = "No authority for this action") :
    Exception(message(base, message))

class LoginException(message: String? = null, base: String = "Invalid authentication") :
    Exception(message(base, message))

open class NotFoundException(message: String? = null, base: String = "Not found") : Exception(message(base, message))

private fun message(base: String, message: String?) = base + if (message != null) ": $message" else ""