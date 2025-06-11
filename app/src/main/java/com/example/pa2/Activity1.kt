package com.example.pa2

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream
import android.content.Intent

class Activity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1)

        val listView = findViewById<ListView>(R.id.reserve_list)
        val reservations = loadReservations()
        val adapter = Activity1Adapter(this, reservations)
        listView.adapter = adapter

        val reserveButton = findViewById<Button>(R.id.reserve)
        reserveButton.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }
    }

    private fun loadReservations(): List<UserInfo> {
        // restaurant_info.txt 파싱
        val restaurantInput: InputStream = assets.open("restaurant_info.txt")
        val restaurantJson = restaurantInput.bufferedReader().use { it.readText() }
        val restaurantArray = JSONArray(restaurantJson)
        val restaurantMap = mutableMapOf<Int, Pair<String, String>>() // id -> (name, image)
        for (i in 0 until restaurantArray.length()) {
            val obj = restaurantArray.getJSONObject(i)
            restaurantMap[obj.getInt("id")] =
                Pair(obj.getString("restaurant"), obj.getString("image"))
        }

        // user_info.txt 파싱 (여기선 첫 번째 유저만 사용)
        val userInput: InputStream = assets.open("user_info.txt")
        val userJson = userInput.bufferedReader().use { it.readText() }
        val userArray = JSONArray(userJson)
        val userObj = userArray.getJSONObject(0) // 첫 번째 유저
        val reservedArray = userObj.getJSONArray("reserved")

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