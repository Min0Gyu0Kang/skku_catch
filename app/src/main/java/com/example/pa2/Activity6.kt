package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity6)

        val restaurantName = intent.getStringExtra("restaurant_name") ?: "Unknown"
        val restaurantImage = intent.getStringExtra("restaurant_image") ?: ""
        val people = intent.getIntExtra("people", 1)
        val date = intent.getStringExtra("date") ?: "00/00/00"
        val time = intent.getStringExtra("time") ?: "00:00"
        val reservationId = intent.getIntExtra("reservation_id", -1)

        findViewById<TextView>(R.id.a6_restaurant).text = restaurantName
        val resId = resources.getIdentifier(restaurantImage, "drawable", packageName)
        findViewById<ImageView>(R.id.a6_figure).setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_background)
        findViewById<TextView>(R.id.a6_people).text = "People: $people"
        findViewById<TextView>(R.id.a6_date).text = "Dates: $date"
        findViewById<TextView>(R.id.a6_time).text = "Times: $time"

        val cancelButton = findViewById<Button>(R.id.a6_cancel)
        cancelButton.setOnClickListener {
            val userId = getUserIdFromEnv()
            if (userId.isEmpty() || reservationId == -1) return@setOnClickListener

            // user_info.txt 읽기
            val file = getFileStreamPath("user_info.txt")
            if (!file.exists()) {
                assets.open("user_info.txt").use { input ->
                    openFileOutput("user_info.txt", MODE_PRIVATE).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            val json = openFileInput("user_info.txt").bufferedReader().use { it.readText() }
            val users = JSONArray(json)

            // 해당 유저의 예약 삭제 (역순 반복)
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("id") == userId) {
                    val reserved = user.getJSONArray("reserved")
                    for (j in reserved.length() - 1 downTo 0) {
                        val res = reserved.getJSONObject(j)
                        if (res.getInt("reservation_id") == reservationId) {
                            reserved.remove(j)
                            break
                        }
                    }
                    break
                }
            }
            openFileOutput("user_info.txt", MODE_PRIVATE).use {
                it.write(users.toString().toByteArray())
            }

            Toast.makeText(this, "Reservations has been canceled.", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                // .env 파일 비우기
                openFileOutput(".env", MODE_PRIVATE).use { it.write("".toByteArray()) }
                val intent = Intent(this, Activity1::class.java)
                startActivity(intent)
            }, 800) // 0.8초 후 이동
        }
    }

    // .env에서 user_id 읽기
    private fun getUserIdFromEnv(): String {
        val file = getFileStreamPath(".env")
        if (!file.exists()) return ""
        val lines = openFileInput(".env").bufferedReader().readLines()
        for (line in lines) {
            if (line.startsWith("user_id=")) {
                return line.substringAfter("=")
            }
        }
        return ""
    }
}
