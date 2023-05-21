package domain.repo

interface Repo<T : Any, ID : Any> {
    suspend fun get(id: ID): T?
    suspend fun getAll(): List<T>
    suspend fun save(entity: T): T
    suspend fun delete(id: ID)
}