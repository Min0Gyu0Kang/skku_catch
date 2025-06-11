package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity4)

        //confirm 버튼 클릭 시 activity 5
        val confirmButton = findViewById<Button>(R.id.a4_button)
        confirmButton.setOnClickListener {
            val intent = Intent(this, Activity5::class.java)
            startActivity(intent)
        }
    }
}