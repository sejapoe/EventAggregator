package usecases.model

data class IdModel(
    val id: Int,
)

data class ListModel<T>(
    val items: List<T>,
    val total: Int
) {
    constructor(items: List<T>) : this(items, items.size)
}