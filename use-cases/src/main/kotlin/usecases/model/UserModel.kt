package usecases.model

import domain.entity.user.*

data class ListUsersModel(
    val items: List<UserModel>,
    val total: Long
)

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

data class RegisterUserModel(
    val email: Email,
    val password: NewPassword,
    val isManager: Boolean = false,
)