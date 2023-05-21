package domain.repo.user

import domain.entity.user.User
import domain.repo.Repo

interface UserRepo : Repo<User, Int> {
    suspend fun findByEmail(email: String): User?
}