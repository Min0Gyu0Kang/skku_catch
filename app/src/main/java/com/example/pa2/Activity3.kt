package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity3)

        val restaurantId = intent.getIntExtra("restaurant_id", 1)
        val restaurant = loadRestaurantById(restaurantId)

        val imageView = findViewById<ImageView>(R.id.a3_figure)
        val detailsView = findViewById<TextView>(R.id.details)
        val menuListView = findViewById<ListView>(R.id.a3_list)

        // 썸네일 이미지 설정
        val resId = resources.getIdentifier(restaurant.image, "drawable", packageName)
        imageView.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_background)

        // 상세 설명
        detailsView.text = restaurant.description

        // 메뉴 리스트 어댑터 연결
        val adapter = Activity3Adapter(this, restaurant.menu)
        menuListView.adapter = adapter

        //reservation 버튼 클릭 시 activity 4
        val reservationButton = findViewById<Button>(R.id.a3_reservation)
        reservationButton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            intent.putExtra("restaurant_id", restaurantId)
            startActivity(intent)
        }
    }

    private fun loadRestaurantById(id: Int): RestaurantDetail {
        val inputStream: InputStream = assets.open("restaurant_info.txt")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            if (obj.getInt("id") == id) {
                val menuArray = obj.getJSONArray("Menu")
                val menuList = mutableListOf<MenuItem>()
                for (j in 0 until menuArray.length()) {
                    val menuObj = menuArray.getJSONObject(j)
                    menuList.add(
                        MenuItem(
                            name = menuObj.getString("name"),
                            price = menuObj.getInt("price"),
                            image = menuObj.getString("image")
                        )
                    )
                }
                return RestaurantDetail(
                    id = obj.getInt("id"),
                    name = obj.getString("restaurant"),
                    image = obj.getString("image"),
                    description = obj.getString("description"),
                    menu = menuList
                )
            }
        }
        // 기본값 반환
        return RestaurantDetail(0, "Unknown", "", "", emptyList())
    }
}

// 메뉴 아이템 데이터 클래스
data class MenuItem(val name: String, val price: Int, val image: String)

// 레스토랑 상세 데이터 클래스
data class RestaurantDetail(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val menu: List<MenuItem>
)