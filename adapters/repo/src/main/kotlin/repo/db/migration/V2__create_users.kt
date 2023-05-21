package repo.db.migration

import domain.entity.user.Authorities
import domain.entity.user.Email
import domain.entity.user.PasswordHash
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.transactions.transaction
import repo.user.AuthorityEntity
import repo.user.UserEntity

@Suppress("ClassName", "unused")
class V2__create_users : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            val user = UserEntity.new {
                email = Email("admin@admin.com")
                password = PasswordHash("\$2a\$12\$/gZjuJsIULVWgL4hUhTJ8OzEGBt1cr74GjBXMgebFN.2WjrX0N9fK")
            }
            Authorities.values().map {
                AuthorityEntity.new {
                    authority = it
                    this.user = user
                }
            }
        }
    }
}
