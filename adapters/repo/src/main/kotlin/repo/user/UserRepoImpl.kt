package repo.user

import domain.entity.user.Email
import domain.entity.user.User
import domain.repo.user.UserRepo
import repo.BaseDAO
import repo.utils.query

@Suppress("unused")
class UserRepoImpl : UserRepo, BaseDAO<User, Int, UserEntity>(UserEntity) {
    override fun UserEntity.toDomain(): User {
        return toUser()
    }

    override suspend fun findByEmail(email: Email): User? = query {
        UserEntity.find { UserTable.email eq email.value }.firstOrNull()?.toUser()
    }

    override suspend fun create(entity: User): User = query {
        val user = UserEntity.new {
            email = entity.email
            password = entity.password
        }

        entity.authorities.forEach {
            AuthorityEntity.new {
                this.user = user
                authority = it
            }
        }
        user.toUser()
    }

    override suspend fun update(existing: UserEntity, entity: User): User = query {
        existing.email = entity.email
        existing.password = entity.password
        val existingAuthorities = existing.authorities.map { it.authority }
        val toDelete = existing.authorities.filter { it.authority !in entity.authorities }
        toDelete.forEach {
            it.delete()
        }
        val newAuthorities = entity.authorities.filter { it !in existingAuthorities }
        newAuthorities.forEach {
            AuthorityEntity.new {
                this.user = existing
                authority = it
            }
        }
        existing.toUser()
    }
}