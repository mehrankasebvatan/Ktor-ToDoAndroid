package ir.kasebvatan.ktor_todo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.kasebvatan.ktor_todo.R
import ir.kasebvatan.ktor_todo.databinding.ActivityRegisterBinding
import ir.kasebvatan.ktor_todo.helper.WaitDialog
import ir.kasebvatan.ktor_todo.model.ResponseModel
import ir.kasebvatan.ktor_todo.model.UserModel
import ir.kasebvatan.ktor_todo.network.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.apply {

            btnRegister.setOnClickListener {
                inputUsername.error = null
                inputPassword.error = null

                if (inputUsername.text.toString().isEmpty()) inputUsername.error = "Please Fill it"
                else if (inputPassword.text.toString().isEmpty()) inputPassword.error =
                    "Please Fill it"
                else register()
            }


            txtLogin.setOnClickListener {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                )
            }

        }

    }

    private fun register() {
        WaitDialog.show(this@RegisterActivity)
        ApiProvider.api.register(
            UserModel(
                username = binding.inputUsername.text.toString(),
                password = binding.inputPassword.text.toString()
            )
        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                WaitDialog.hide()

                try {
                    Toast.makeText(
                        this@RegisterActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    )
                        .show()

                    if (response.body()?.code == 1) {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }


                } catch (e: Exception) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "FAILED REQUEST $e",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                WaitDialog.hide()
                Toast.makeText(this@RegisterActivity, "FAILED REQUEST: $t", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }


}