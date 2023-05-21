package usecases.model

import domain.entity.organizer.Organizer

data class OrganizerModel(
    val id: Int,
    val name: String,
    val description: String,
    val user: UserModel
) {
    constructor(organizer: Organizer) : this(
        organizer.id,
        organizer.name,
        organizer.description,
        UserModel(organizer.user)
    )
}

data class NewOrganizerModel(
    val name: String,
    val description: String,
)

data class UpdateOrganizerModel(
    val id: Int,
    val name: String,
    val description: String,
)