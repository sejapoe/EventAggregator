package usecases.model

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.Password
import domain.entity.user.User

data class UserModel(
    val id: Int,
    val email: Email,
    val authorities: List<Authorities> = listOf(),
) {
    constructor(user: User) : this(
        id = user.id,
        email = user.email,
        authorities = user.authorities,
    )
}


data class LoginUserModel(
    val email: Email,
    val password: Password
)