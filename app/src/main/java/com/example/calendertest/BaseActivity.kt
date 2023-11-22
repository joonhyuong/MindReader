package com.example.calendertest

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SharedPreferences에서 저장된 색상 값을 읽어오기
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val defaultColor = Color.parseColor("#F6EFE7")
        val selectedColor = sharedPref.getInt("selected_color", defaultColor) // defaultColor는 기본값

        // 배경색 설정
        setActivityBackgroundColor(selectedColor)
    }

    // 액티비티의 배경색 설정
    fun setActivityBackgroundColor(color: Int) {
        window.decorView.setBackgroundColor(color)
    }
}