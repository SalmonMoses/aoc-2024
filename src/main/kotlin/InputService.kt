package me.salmonmoses

import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.annotation.Property
import org.koin.core.annotation.Single
import java.io.File

@Single
class InputService(val httpClient: OkHttpClient, @Property("session") val session: String) {
    fun checkDayInput(day: Int): Boolean = File("input/day$day.txt").exists()

    fun downloadDayInput(day: Int) {
        val url = "https://adventofcode.com/2024/day/$day/input"
        val outputFile = "input/day${day}.txt"
        val request = Request.Builder()
            .url(url)
            .header("Cookie", "session=$session")
            .build()
        httpClient.newCall(request)
            .execute()
            .use { response ->
                response.body?.byteStream()?.use { input -> File(outputFile).outputStream().use { input.copyTo(it) } }
            }
    }

    fun getDayInput(day: Int): List<String> = File("input/", "day$day.txt").readLines()

}