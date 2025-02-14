package ir.kasebvatan.ktor_todo.network

import android.util.Log
import ir.kasebvatan.ktor_todo.helper.Constant
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiProvider {

    // Retrofit instance (initialized dynamically)
    private var retrofit: Retrofit? = null

    // API Service instance
    val api: ApiService
        get() {
            if (retrofit == null) {
                retrofit = createRetrofit()
            }
            return retrofit!!.create(ApiService::class.java)
        }

    // Function to recreate Retrofit instance when BASE_URL changes
    fun recreateApi() {
        retrofit = createRetrofit()
    }

    // Function to create a new Retrofit instance
    private fun createRetrofit(): Retrofit {
        val loggingInterceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Chain): Response {
                val request = chain.request()
                val requestBody = request.body

                // Log the endpoint and request body
                val endpoint = request.url.toUri().path
                if (requestBody != null) {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)
                    val bodyString = buffer.readUtf8()
                    Log.d("Retrofit", "Request Body (${request.url}) ===> \n$bodyString")
                }

                // Proceed with the request
                val response = chain.proceed(request)

                // Log the response body
                val responseBody = response.body
                if (responseBody != null) {
                    val bodyString = responseBody.string()
                    Log.d("Retrofit", "Response Body (${request.url}) ===> \n$bodyString")
                    // Re-create the response body since it's a one-time read
                    return response.newBuilder()
                        .body(bodyString.toResponseBody(responseBody.contentType())).build()
                }

                return response
            }
        }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            //.addInterceptor(RetryInterceptor(5, 1000)) // Uncomment if needed
            .build()

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL) // Always fetch latest BASE_URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}







