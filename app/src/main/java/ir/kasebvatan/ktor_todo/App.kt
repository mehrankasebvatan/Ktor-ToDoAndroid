package ir.kasebvatan.ktor_todo

import android.app.Application
import ir.kasebvatan.ktor_todo.helper.Cache

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Cache.init(this)
    }


}