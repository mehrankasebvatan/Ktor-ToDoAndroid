package ir.kasebvatan.ktor_todo.helper

import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {

    fun now(): String {
        val day = String.format(Locale("en"), "%02d", PersianDate.today().shDay)
        val month = String.format(Locale("en"), "%02d", PersianDate.today().shMonth)
        val year = String.format(Locale("en"), "%04d", PersianDate.today().shYear)

        val durationFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = Date()

        val d = durationFormat.format(currentTime)


        return "$year/$month/$day  $d"
    }
}