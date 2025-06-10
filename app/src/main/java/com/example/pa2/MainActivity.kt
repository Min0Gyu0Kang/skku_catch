package com.example.pa2

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import android.content.Intent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity0)

        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            // 로그인 검증 로직 추가 가능
            val intent = Intent(this, Activity1::class.java)
            startActivity(intent)
            finish()
        }
    }
}

