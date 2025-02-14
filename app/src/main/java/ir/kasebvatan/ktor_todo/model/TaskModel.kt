package ir.kasebvatan.ktor_todo.model

data class TaskModel(
    val userId: Int,
    val id: Int?,
    var title: String,
    var content: String,
    var lastModifiedDate: String
)
