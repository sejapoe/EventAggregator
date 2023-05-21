package repo.user

import domain.entity.user.*
import kotlinx.coroutines.runBlocking
import repo.DatabaseFactory
import kotlin.test.*

class UserRepoTest {
    private val email = Email("admin@admin.com")
    private val password = Password("test")
    private val passwordHash = PasswordHash(password.value.reversed())

    private val user = User(
        id = -1,
        email = email,
        authorities = listOf(Authorities.USER, Authorities.ADMIN),
        password = passwordHash
    )

    @BeforeTest
    fun setUp() {
        DatabaseFactory.init("org.h2.Driver", "jdbc:h2:mem:", "test", drop = true, test = true)
    }

    @Test
    fun `User tests`() {
        runBlocking {
            val repository = UserRepoImpl()
            // User does not exists
            assertNull(repository.findByEmail(email))
            // No users exist
            assertEquals(0, repository.getAll().size)

            // Create user
            val id = repository.save(user).id
            val created = repository.get(id)
            assertNotNull(created)
            // Database should generate new id
            assertNotEquals(user.id, id)
            // Other data should be equal
            assertEquals(user.email, created.email)
            assertEquals(user.authorities, created.authorities)
            assertEquals(user.password, created.password)

            // Get created user
            assertEquals(created, repository.findByEmail(email))
            // Get all created users
            assertEquals(1, repository.getAll().size)
            assertEquals(listOf(created), repository.getAll())

            // Delete user
            repository.delete(id)
            assertEquals(0, repository.getAll().count())
        }
    }
}