package domain.entity.event

import domain.InvalidPropertyException
import domain.NotFoundException

class EventNotFoundException(message: String? = null, base: String = "Event not found") :
    NotFoundException(message, base)

class DateInvalidException(message: String? = null, base: String = "Invalid date") :
    InvalidPropertyException(message, base)

class EventInvalidException(message: String? = null, base: String = "Invalid event") :
    InvalidPropertyException(message, base)