package ir.kasebvatan.ktor_todo.model

data class ResponseModel(
    val code: Int? = -1,
    val message: String? = "",
    val data: List<TaskModel>? = emptyList()
)
