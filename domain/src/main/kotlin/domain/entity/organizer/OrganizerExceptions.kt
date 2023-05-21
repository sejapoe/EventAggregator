package domain.entity.organizer

import domain.InvalidPropertyException
import domain.NotFoundException

class OrganizerInvalidException(message: String? = null, base: String = "Invalid organizer") :
    InvalidPropertyException(message, base)

class OrganizerNotFoundException(message: String? = null, base: String = "Organizer not found") :
    NotFoundException(message, base)