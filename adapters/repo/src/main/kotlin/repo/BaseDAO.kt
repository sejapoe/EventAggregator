package repo

import domain.repo.Repo
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import repo.utils.query

abstract class BaseDAO<Domain : domain.entity.Entity<ID>, ID : Comparable<ID>, E : Entity<ID>>(
    private val dao: EntityClass<ID, E>
) : Repo<Domain, ID> {
    abstract fun E.toDomain(): Domain

    override suspend fun get(id: ID): Domain? = query {
        dao.findById(id)?.toDomain()
    }

    override suspend fun getAll(): List<Domain> = query {
        dao.all().map { it.toDomain() }
    }

    override suspend fun delete(id: ID) = query {
        dao[id].delete()
    }

    override suspend fun save(entity: Domain): Domain = query {
        val id = entity.id
        dao.findById(id)?.let {
            update(it, entity)
        } ?: create(entity)
    }

    abstract suspend fun create(entity: Domain): Domain
    abstract suspend fun update(existing: E, entity: Domain): Domain
}