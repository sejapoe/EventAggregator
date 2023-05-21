package domain.entity.user

import domain.AlreadyExistsException
import domain.BaseException
import domain.InvalidPropertyException

class EmailInvalidException(message: String? = null, base: String = "Email invalid") :
    InvalidPropertyException(message, base)

class PasswordInvalidException(message: String? = null, base: String = "Password invalid") :
    InvalidPropertyException(message, base)

class UserAlreadyExistsException(message: String? = null, base: String = "User already exists") :
    AlreadyExistsException(message, base)

class AuthException(message: String? = null, base: String = "No authority for this action") :
    BaseException(base, message)

class LoginException(message: String? = null, base: String = "Invalid authentication") :
    BaseException(base, message)