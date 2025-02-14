package ir.kasebvatan.ktor_todo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.kasebvatan.ktor_todo.R
import ir.kasebvatan.ktor_todo.databinding.ActivityLoginBinding
import ir.kasebvatan.ktor_todo.helper.Cache
import ir.kasebvatan.ktor_todo.helper.WaitDialog
import ir.kasebvatan.ktor_todo.model.ResponseModel
import ir.kasebvatan.ktor_todo.model.UserModel
import ir.kasebvatan.ktor_todo.network.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            btnLogin.setOnClickListener {
                inputUsername.error = null
                inputPassword.error = null

                if (inputUsername.text.toString().isEmpty()) inputUsername.error = "Please Fill it"
                else if (inputPassword.text.toString().isEmpty()) inputPassword.error =
                    "Please Fill it"
                else login()
            }

            txtRegister.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        RegisterActivity::class.java
                    )
                )
            }
        }


    }

    private fun login() {
        WaitDialog.show(this@LoginActivity)
        ApiProvider.api.login(
            UserModel(
                username = binding.inputUsername.text.toString(),
                password = binding.inputPassword.text.toString()
            )
        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                WaitDialog.hide()

                try {
                    Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_LONG)
                        .show()

                    if ((response.body()?.code ?: 0) > 0) {
                        Cache["login"] = true
                        Cache["userId"] = response.body()?.code
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }


                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "FAILED REQUEST $e", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                WaitDialog.hide()
                Toast.makeText(this@LoginActivity, "FAILED REQUEST: $t", Toast.LENGTH_LONG).show()
            }
        })
    }
}