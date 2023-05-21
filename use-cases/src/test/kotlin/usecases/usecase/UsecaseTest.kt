package usecases.usecase

interface UsecaseTest {
    val usecase: UsecaseType<*>

    fun success()
    fun unauthenticated()
    fun `No user rules`()
}