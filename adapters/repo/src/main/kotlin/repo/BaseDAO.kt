package repo

import domain.repo.Repo
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import repo.utils.query

abstract class BaseDAO<Domain : Any, ID : Comparable<ID>, E : Entity<ID>>(
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
}