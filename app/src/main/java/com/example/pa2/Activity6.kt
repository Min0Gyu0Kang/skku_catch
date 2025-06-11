package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.InputStream

class Activity6 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity6)

        //cancel 버튼 클릭 시 activity 1
        val cancelButton = findViewById<Button>(R.id.a6_cancel)
        cancelButton.setOnClickListener {
            val intent = Intent(this, Activity1::class.java)
            startActivity(intent)
        }
    }
}
