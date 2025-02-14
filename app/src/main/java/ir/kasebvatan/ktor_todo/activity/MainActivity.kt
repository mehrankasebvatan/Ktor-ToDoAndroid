package ir.kasebvatan.ktor_todo.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.kasebvatan.ktor_todo.NoteSheet
import ir.kasebvatan.ktor_todo.R
import ir.kasebvatan.ktor_todo.adapter.MainAdapter
import ir.kasebvatan.ktor_todo.databinding.ActivityMainBinding
import ir.kasebvatan.ktor_todo.helper.Cache
import ir.kasebvatan.ktor_todo.helper.WaitDialog
import ir.kasebvatan.ktor_todo.model.ResponseModel
import ir.kasebvatan.ktor_todo.model.TaskModel
import ir.kasebvatan.ktor_todo.network.ApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getData()

        binding.apply {
            txtLogout.setOnClickListener {
                Cache["login"] = false
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }

            txtRefresh.setOnClickListener { getData() }

            btnAdd.setOnClickListener {
                openNoteDialog(null)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        binding.apply {
            progress.visibility = View.VISIBLE
            txtNoData.visibility = View.GONE

            ApiProvider.api.getTasks(Cache["userId", -1])
                .enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(
                        call: Call<ResponseModel>,
                        response: Response<ResponseModel>
                    ) {
                        progress.visibility = View.GONE

                        try {

                            if (response.body()?.code == 1) {
                                rvData.adapter = MainAdapter(
                                    response.body()?.data ?: emptyList()
                                ) { what, model ->
                                    when (what) {
                                        "edit" -> {
                                            openNoteDialog(model)
                                        }

                                        "delete" -> {
                                            delete(model)
                                        }
                                    }

                                }
                                rvData.visibility = View.VISIBLE
                            } else {
                                txtNoData.visibility = View.VISIBLE
                                txtNoData.text = response.body()?.message
                            }

                        } catch (e: Exception) {
                            txtNoData.text = "catch: $e"
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        progress.visibility = View.GONE
                        txtNoData.visibility = View.VISIBLE
                        txtNoData.text = "onFailure: $t"
                    }
                })

        }
    }


    private fun delete(model: TaskModel) {
        WaitDialog.show(this@MainActivity)
        ApiProvider.api.deleteTask(model.id ?: -1)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    WaitDialog.hide()

                    try {

                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (response.body()?.code == 1) {
                            getData()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(this@MainActivity, "catch: $e", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    WaitDialog.hide()
                    Toast.makeText(this@MainActivity, "onFailure: $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun save(model: TaskModel) {
        WaitDialog.show(this@MainActivity)
        ApiProvider.api.addTask(model)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    WaitDialog.hide()

                    try {

                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (response.body()?.code == 1) {
                            getData()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(this@MainActivity, "catch: $e", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    WaitDialog.hide()
                    Toast.makeText(this@MainActivity, "onFailure: $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun edit(model: TaskModel) {
        WaitDialog.show(this@MainActivity)
        ApiProvider.api.updateTask(model)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    WaitDialog.hide()

                    try {

                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        if (response.body()?.code == 1) {
                            getData()
                        }

                    } catch (e: Exception) {

                        Toast.makeText(this@MainActivity, "catch: $e", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    WaitDialog.hide()
                    Toast.makeText(this@MainActivity, "onFailure: $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openNoteDialog(model: TaskModel?) {
        NoteSheet(
            this@MainActivity,
            model
        ) { data, what ->
            when (what) {
                "edit" -> edit(data)
                "save" -> save(data)
            }

        }.show()
    }

}