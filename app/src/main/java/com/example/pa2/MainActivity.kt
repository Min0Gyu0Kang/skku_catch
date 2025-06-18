package com.example.pa2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.content.Intent
import org.json.JSONArray
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity0)

        val loginButton = findViewById<Button>(R.id.reserve)
        val idEdit = findViewById<EditText>(R.id.userID)
        val pwEdit = findViewById<EditText>(R.id.userPW)

        loginButton.setOnClickListener {
            val inputId = idEdit.text.toString()
            val inputPw = pwEdit.text.toString()
            val (success, reason, userObj) = checkLogin(inputId, inputPw)
            if (success && userObj != null) {
                val infoObj = userObj.getJSONObject("info")
                val intent = Intent(this, Activity1::class.java)
                intent.putExtra("user_id", userObj.getString("id"))
                intent.putExtra("user_name", infoObj.getString("name"))
                intent.putExtra("user_age", infoObj.getInt("age"))
                intent.putExtra("user_gender", infoObj.getString("gender"))
                val reservedArray = userObj.getJSONArray("reserved")
                intent.putExtra("user_reserved", reservedArray.toString())
                intent.putExtra("user_id", userObj.getString("id"))
                //.env 초기화
                openFileOutput(".env", MODE_PRIVATE).use { it.write("".toByteArray()) }
                // user_id를 .env 파일에 저장
                openFileOutput(".env", MODE_PRIVATE).use {
                    it.write("user_id=${userObj.getString("id")}".toByteArray())
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                if (reason == "id") {
                    Toast.makeText(this, "ID is not assigned", Toast.LENGTH_SHORT).show()
                } else if (reason == "pw") {
                    Toast.makeText(this, "Password is wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLogin(
        inputId: String,
        inputPw: String
    ): Triple<Boolean, String?, org.json.JSONObject?> {
        val inputStream: InputStream = assets.open("user_info.txt")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val id = obj.getString("id")
            val pw = obj.getString("passwd")
            if (id == inputId) {
                return if (pw == inputPw) Triple(true, null, obj)
                else Triple(false, "pw", null)
            }
        }
        return Triple(false, "id", null)
    }
}