package com.example.pa2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import java.util.*
import android.app.AlertDialog

class Activity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity4)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val calendarView = findViewById<CalendarView>(R.id.a4_calendarView)

        // 1~10 인원 Spinner 세팅 (이외 값 선택 불가)
        val peopleList = (1..10).map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, peopleList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 오늘 날짜로 minDate 설정 (오늘 이전 선택 불가)
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        calendarView.minDate = today.timeInMillis

        var selectedDate: Long = calendarView.date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            selectedDate = cal.timeInMillis
        }

        //confirm 버튼 클릭 시 activity 5
        val confirmButton = findViewById<Button>(R.id.a4_button)
        confirmButton.setOnClickListener {
            val people = spinner.selectedItem.toString().toInt()
            val cal = Calendar.getInstance()
            cal.timeInMillis = selectedDate
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1 // 0-based
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dateStr = String.format("%02d/%02d/%02d", year % 100, month, day)
            val restaurantId = intent.getIntExtra("restaurant_id", 1)
            val userId = intent.getStringExtra("user_id") ?: ""
            val intent = Intent(this, Activity5::class.java)
            intent.putExtra("people", people)
            intent.putExtra("date", dateStr)
            intent.putExtra("restaurant_id", restaurantId)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }
}