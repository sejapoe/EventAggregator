package repo.user

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import domain.entity.user.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

internal object UserTable : IntIdTable() {
    val email = varchar("email", 50).index(isUnique = true)
    val password = varchar("password", 200)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    var email by UserTable.email.transform({ it.value }, { Email(it) })
    val authorities by AuthorityEntity referrersOn AuthorityTable.user
    var password by UserTable.password.transform({ it.value }, { PasswordHash(it) })

    fun toUser() = User(id.value, email, authorities.map { it.authority }, password)

    companion object : IntEntityClass<UserEntity>(UserTable)
}

internal object AuthorityTable : IntIdTable() {
    val user = reference("user", UserTable, onDelete = ReferenceOption.CASCADE)
    val authority = enumeration<Authorities>("authority")
}

class AuthorityEntity(id: EntityID<Int>) : IntEntity(id) {
    var user by UserEntity referencedOn AuthorityTable.user
    var authority by AuthorityTable.authority

    companion object : IntEntityClass<AuthorityEntity>(AuthorityTable)
}