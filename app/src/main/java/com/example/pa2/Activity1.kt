package com.example.pa2

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream
import android.content.Intent
import android.util.Log
import android.widget.TextView

class Activity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1)
        // .env에서 user_id 읽기
        val userId = getUserIdFromEnv()
        if (userId.isEmpty()) {
            // 로그인 정보 없음 처리
            finish()
            return
        }

        // user_info.txt에서 최신 유저 정보 읽기
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
        var userObj: org.json.JSONObject? = null
        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            if (user.getString("id") == userId) {
                userObj = user
                break
            }
        }
        if (userObj == null) {
            finish()
            return
        }
        val infoObj = userObj.getJSONObject("info")
        val reservedArray = userObj.getJSONArray("reserved")
        val userText =
            "$userId: (${infoObj.getString("name")}/${infoObj.getInt("age")}/${infoObj.getString("gender")})"
        findViewById<TextView>(R.id.a1_user).text = userText

        val listView = findViewById<ListView>(R.id.reserve_list)
        val reservations = loadReservations(reservedArray)
        val adapter = Activity1Adapter(this, reservations)
        listView.adapter = adapter
        val reserveButton = findViewById<Button>(R.id.reserve)
        reserveButton.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }

    private fun loadReservations(reservedArray: JSONArray): List<UserInfo> {
        val restaurantInput: InputStream = assets.open("restaurant_info.txt")
        val restaurantJson = restaurantInput.bufferedReader().use { it.readText() }
        val restaurantArray = JSONArray(restaurantJson)
        val restaurantMap = mutableMapOf<Int, Pair<String, String>>()
        for (i in 0 until restaurantArray.length()) {
            val obj = restaurantArray.getJSONObject(i)
            restaurantMap[obj.getInt("id")] =
                Pair(obj.getString("restaurant"), obj.getString("image"))
        }

        val reservationList = mutableListOf<UserInfo>()
        for (i in 0 until reservedArray.length()) {
            val resObj = reservedArray.getJSONObject(i)
            val restaurantId = resObj.getInt("restaurant_id")
            val (restaurantName, restaurantImage) = restaurantMap[restaurantId] ?: Pair(
                "Unknown",
                ""
            )
            reservationList.add(
                UserInfo(
                    restaurantName,
                    restaurantImage,
                    resObj.getString("time"),
                    resObj.getString("date"),
                    resObj.getInt("number_of_people")
                )
            )
        }
        return reservationList
    }

    // .env에서 user_id 읽기 함수 추가
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