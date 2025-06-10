package com.example.pa2

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1)

        val listView = findViewById<ListView>(R.id.reserve_list)
        val restaurants = loadRestaurants()
        val adapter = RestaurantAdapter(this, restaurants)
        listView.adapter = adapter
    }

    private fun loadRestaurants(): List<Restaurant> {
        val inputStream: InputStream = assets.open("restaurant_info.txt")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val list = mutableListOf<Restaurant>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                Restaurant(
                    id = obj.getInt("id"),
                    name = obj.getString("restaurant"),
                    type = obj.getString("type"),
                    location = obj.getString("location"),
                    rating = obj.getString("rating"),
                    image = obj.getString("image")
                )
            )
        }
        return list
    }
}