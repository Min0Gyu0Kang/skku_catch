package com.example.pa2

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream
import android.content.Intent
import android.util.Log

class Activity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)

        val userId = intent.getStringExtra("user_id") ?: ""
        val (restaurants, openingHours) = loadRestaurantsWithOpeningHours()
        val listView = findViewById<ListView>(R.id.a2_reservations)
        val adapter = Activity2Adapter(this, restaurants, openingHours)
        listView.adapter = adapter
        // 리스트 아이템 클릭 시 Activity3로 이동
        listView.setOnItemClickListener { _, _, position, _ ->
            val restaurant = restaurants[position]
            val intent = Intent(this, Activity3::class.java)
            intent.putExtra("restaurant_id", restaurant.id)
            intent.putExtra("user_id", intent.getStringExtra("user_id"))
            startActivity(intent)
        }
    }

    private fun loadRestaurantsWithOpeningHours(): Pair<List<RestaurantInfo>, List<Pair<String, String>>> {
        val inputStream: InputStream = assets.open("restaurant_info.txt")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val list = mutableListOf<RestaurantInfo>()
        val hours = mutableListOf<Pair<String, String>>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                RestaurantInfo(
                    id = obj.getInt("id"),
                    name = obj.getString("restaurant"),
                    type = obj.getString("type"),
                    location = obj.getString("location"),
                    rating = obj.getString("rating"),
                    image = obj.getString("image")
                )
            )
            val opening = obj.getJSONObject("openingHours")
            hours.add(Pair(opening.getString("open"), opening.getString("close")))
        }
        return Pair(list, hours)
    }
}