package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity6)

        // Activity 전달받은 값 읽기
        val restaurantName = intent.getStringExtra("restaurant_name") ?: "Unknown"
        val restaurantImage = intent.getStringExtra("restaurant_image") ?: ""
        val people = intent.getIntExtra("people", 1)
        val date = intent.getStringExtra("date") ?: "00/00/00"
        val time = intent.getStringExtra("time") ?: "00:00"

        // 뷰에 표시
        findViewById<TextView>(R.id.a6_restaurant).text = restaurantName
        val resId = resources.getIdentifier(restaurantImage, "drawable", packageName)
        findViewById<ImageView>(R.id.a6_figure).setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_background)
        findViewById<TextView>(R.id.a6_people).text = "People: $people"
        findViewById<TextView>(R.id.a6_date).text = "Dates: $date"
        findViewById<TextView>(R.id.a6_time).text = "Times: $time"

        //cancel 버튼 클릭 시 에약 내역 지우고 activity 1
        val cancelButton = findViewById<Button>(R.id.a6_cancel)
        cancelButton.setOnClickListener {
            val userId = intent.getStringExtra("user_id") ?: return@setOnClickListener
            val reservationId = intent.getIntExtra("reservation_id", -1)
            if (reservationId == -1) return@setOnClickListener

            // user_info.txt 읽기
            val inputStream = assets.open("user_info.txt")
            val json = inputStream.bufferedReader().use { it.readText() }
            val users = JSONArray(json)

            // 해당 유저 찾기
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("id") == userId) {
                    val reserved = user.getJSONArray("reserved")
                    // 예약 삭제
                    for (j in 0 until reserved.length()) {
                        val res = reserved.getJSONObject(j)
                        if (res.getInt("reservation_id") == reservationId) {
                            reserved.remove(j)
                            break
                        }
                    }
                    break
                }
            }

            val intent = Intent(this, Activity1::class.java)
            startActivity(intent)
        }
    }
}
