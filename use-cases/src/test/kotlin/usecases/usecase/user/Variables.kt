package usecases.usecase.user

import domain.entity.user.*
import usecases.model.UserModel

const val id = 1
val email = Email("test@test.com")
val password = Password("test")
val passwordHash = PasswordHash(password.value.reversed())
val user = User(id = id, email = email, authorities = listOf(Authorities.USER), password = passwordHash)
val userModel = UserModel(user)