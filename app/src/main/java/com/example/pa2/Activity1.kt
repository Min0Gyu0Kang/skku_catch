package com.example.pa2

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream
import android.content.Intent
import android.widget.TextView

class Activity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1)

        // activity0 유저 정보 가져오기
        val userId = intent.getStringExtra("user_id") ?: ""
        val userName = intent.getStringExtra("user_name") ?: ""
        val userAge = intent.getIntExtra("user_age", 0)
        val userGender = intent.getStringExtra("user_gender") ?: ""

        val userText = "$userId: ($userName/$userAge/$userGender)"
        findViewById<TextView>(R.id.a1_user).text = userText

        val reservedJson = intent.getStringExtra("user_reserved") ?: "[]"
        val reservedArray = JSONArray(reservedJson)
        val listView = findViewById<ListView>(R.id.reserve_list)
        val reservations = loadReservations(reservedArray)
        val adapter = Activity1Adapter(this, reservations)
        listView.adapter = adapter

        val reserveButton = findViewById<Button>(R.id.reserve)
        reserveButton.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
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
}