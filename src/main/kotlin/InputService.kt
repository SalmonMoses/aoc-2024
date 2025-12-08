package me.salmonmoses

import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import java.io.File

@Single
class InputService(val httpClient: OkHttpClient, @Property("session") val session: String) {
    fun checkDayInput(day: Int, year: Int): Boolean = File("input/$year/day$day.txt").exists()

    fun downloadDayInput(day: Int, year: Int) {
        val url = "https://adventofcode.com/$year/day/$day/input"
        val outputFile = "input/$year/day${day}.txt"
        val request = Request.Builder()
                .url(url)
                .header("Cookie", "session=$session")
                .build()
        httpClient.newCall(request)
                .execute()
                .use { response ->
                    response.body?.byteStream()
                            ?.use { input -> File(outputFile).outputStream().use { input.copyTo(it) } }
                }
    }

    fun getDayInput(day: Int, year: Int): List<String> = File("input/$year", "day$day.txt").readLines()
}