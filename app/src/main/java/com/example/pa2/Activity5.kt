package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity5)

        //confirm 버튼 클릭 시 activity 6
        val confirmButton = findViewById<Button>(R.id.a5_confirm)
        confirmButton.setOnClickListener {
            val intent = Intent(this, Activity6::class.java)
            startActivity(intent)
        }
        //cancel 버튼 클릭 시 activity 4
        val cancelButton = findViewById<Button>(R.id.a5_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }
    }
}