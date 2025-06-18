package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.File
import java.io.InputStream
import android.util.Log

class Activity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity5)

        val userId = intent.getStringExtra("user_id") ?: ""
        val people = intent.getIntExtra("people", 1)
        val date = intent.getStringExtra("date") ?: "00/00/00"
        val restaurantId = intent.getIntExtra("restaurant_id", 1)

        // restaurant_info.txt에서 open/close 시간 읽기
        val (restaurantName, restaurantImage, open, close) = getRestaurantInfo(restaurantId)

        // 화면에 표시
        findViewById<TextView>(R.id.a5_people).text = "People: $people"
        findViewById<TextView>(R.id.a5_date).text = "Dates: $date"
        findViewById<TextView>(R.id.a5_opening2).text = "$open ~ $close"

        val timePicker = findViewById<TimePicker>(R.id.a5_time_reserve)
        timePicker.setIs24HourView(true)
        timePicker.hour = 1
        timePicker.minute = 0 // 기본값 1:00

        //confirm 버튼 클릭 시 activity 6
        val confirmButton = findViewById<Button>(R.id.a5_confirm)
        confirmButton.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val timeStr = String.format("%02d:%02d", hour, minute)
            if (!isValidTime(timeStr, open, close)) {
                Toast.makeText(
                    this,
                    "The reservation time has exceeded the valid opening and closing hours!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // .env에서 user_id 읽기
            val userId = getUserIdFromEnv()
            if (userId.isEmpty()) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 내부 저장소에서 user_info.txt 읽기 (없으면 assets에서 복사)
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

            // 예약 id 생성 (전체 reserved 중 최대값+1)
            var maxId = 0
            for (i in 0 until users.length()) {
                val reserved = users.getJSONObject(i).getJSONArray("reserved")
                for (j in 0 until reserved.length()) {
                    val res = reserved.getJSONObject(j)
                    maxId = maxOf(maxId, res.getInt("reservation_id"))
                }
            }
            val newReservationId = maxId + 1

            // 해당 유저 reserved에 예약 추가
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("id") == userId) {
                    val reserved = user.getJSONArray("reserved")
                    val newRes = org.json.JSONObject()
                    newRes.put("reservation_id", newReservationId)
                    newRes.put("restaurant_id", restaurantId)
                    newRes.put("number_of_people", people)
                    // date는 "YY/MM/DD" → "YYYY-MM-DD"로 변환 필요
                    val dateParts = date.split("/")
                    val fullDate = "20${dateParts[0]}-${dateParts[1]}-${dateParts[2]}"
                    newRes.put("date", fullDate)
                    newRes.put("time", timeStr)
                    reserved.put(newRes)
                    break
                }
            }
            // 예약 추가 직전
            Log.d(
                "Activity5",
                "예약 추가: userId=$userId, restaurantId=$restaurantId, date=$date, time=$timeStr"
            )

            // 파일에 저장
            openFileOutput("user_info.txt", MODE_PRIVATE).use {
                it.write(users.toString().toByteArray())
            }

            val intent = Intent(this, Activity6::class.java)
            intent.putExtra("restaurant_name", restaurantName)
            intent.putExtra("restaurant_image", restaurantImage)
            intent.putExtra("people", people)
            intent.putExtra("date", date)
            intent.putExtra("time", timeStr)
            intent.putExtra("user_id", userId)
            intent.putExtra("reservation_id", newReservationId)
            startActivity(intent)
        }
        //cancel 버튼 클릭 시 activity 4
        val cancelButton = findViewById<Button>(R.id.a5_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }
    }

    // restaurant_info.txt에서 레스토랑 이름, 이미지, open, close 반환
    private fun getRestaurantInfo(restaurantId: Int): Quad<String, String, String, String> {
        val inputStream: InputStream = assets.open("restaurant_info.txt")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getInt("id") == restaurantId) {
                val opening = obj.getJSONObject("openingHours")
                return Quad(
                    obj.getString("restaurant"),
                    obj.getString("image"),
                    opening.getString("open"),
                    opening.getString("close")
                )
            }
        }
        return Quad("Unknown", "", "00:00", "00:00")
    }

    // 시간 유효성 검사 (HH:MM, open <= time < close)
    private fun isValidTime(time: String, open: String, close: String): Boolean {
        val regex = Regex("^\\d{2}:\\d{2}$")
        if (!regex.matches(time)) return false
        val (h, m) = time.split(":").map { it.toIntOrNull() ?: return false }
        val (oh, om) = open.split(":").map { it.toIntOrNull() ?: return false }
        val (ch, cm) = close.split(":").map { it.toIntOrNull() ?: return false }
        val t = h * 60 + m
        val ot = oh * 60 + om
        val ct = ch * 60 + cm
        return t in ot until ct
    }

    // 4개 값 반환용 데이터 클래스
    data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    //.env 파일에서 user_id 읽기
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