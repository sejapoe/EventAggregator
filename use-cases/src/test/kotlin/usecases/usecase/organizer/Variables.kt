package usecases.usecase.organizer

import domain.entity.organizer.Organizer
import domain.entity.user.Authorities
import usecases.model.OrganizerModel
import usecases.model.UserModel
import usecases.usecase.user.user

const val id = 1
const val name = "test"
const val description = "test"
val organizerUser = user.copy(authorities = listOf(Authorities.ORGANIZER, Authorities.USER))
val organizerUserModel = UserModel(organizerUser)
val organizer = Organizer(
    id = id,
    name = name,
    description = description,
    user = organizerUser
)
val organizerModel = OrganizerModel(organizer)