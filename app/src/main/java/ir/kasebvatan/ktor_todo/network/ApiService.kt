package ir.kasebvatan.ktor_todo.network


import ir.kasebvatan.ktor_todo.model.IdModel
import ir.kasebvatan.ktor_todo.model.ResponseModel
import ir.kasebvatan.ktor_todo.model.TaskModel
import ir.kasebvatan.ktor_todo.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    fun register(@Body userModel: UserModel): Call<ResponseModel>

    @POST("login")
    fun login(@Body userModel: UserModel): Call<ResponseModel>

    @GET("getTasks")
    fun getTasks(@Query("id") id: Int): Call<ResponseModel>

    @POST("addTask")
    fun addTask(@Body taskModel: TaskModel): Call<ResponseModel>

    @PUT("updateTask")
    fun updateTask(@Body taskModel: TaskModel): Call<ResponseModel>

    @DELETE("deleteTask")
    fun deleteTask(@Query("id") id: Int): Call<ResponseModel>


}