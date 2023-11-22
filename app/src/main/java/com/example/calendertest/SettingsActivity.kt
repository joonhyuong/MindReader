package com.example.calendertest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import yuku.ambilwarna.AmbilWarnaDialog;


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        lateinit var navigationView: BottomNavigationView

        navigationView = findViewById(R.id.navigationView)
        val removeallbtn = findViewById<Button>(R.id.removeall)
        val select_bgd = findViewById<Button>(R.id.select_bgd)
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedColor = sharedPref.getInt("selected_color", defaultColor) // defaultColor는 기본값


        fun setActivityBackgroundColor(color: Int) {
            window.decorView.setBackgroundColor(color)
        }

        setActivityBackgroundColor(selectedColor)



        removeallbtn.setOnClickListener {
            removeAllFilesFromInternalStorage()
        }

        select_bgd.setOnClickListener {
            showColorPickerDialog()
        }

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> {
                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.homeFragment -> {
                    val intent = Intent(this@SettingsActivity, HistoryActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myPageFragment -> {
                    val intent = Intent(this@SettingsActivity, SettingsActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
    private fun removeAllFilesFromInternalStorage() {
        val directory: File = filesDir // 내부 저장소 디렉토리 가져오기
        val files: Array<String>? = directory.list() // 내부 저장소의 모든 파일 목록 가져오기

        files?.forEach { fileName ->
            val file = File(directory, fileName)
            val isDeleted = file.delete() // 파일 삭제 시도
            if (isDeleted) {
                Toast.makeText(this, "모든 파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "파일을 삭제하는 도중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val defaultColor = Color.parseColor("#F6EFE7")
    var selectedColor: Int = defaultColor // 기본값은 흰색
//
//    // 색상 선택 다이얼로그 표시
//    fun showColorPickerDialog() {
//        val colorPicker = AmbilWarnaDialog(this, selectedColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
//            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
//                selectedColor = color // 사용자가 선택한 색상 저장
//                setActivityBackgroundColor(selectedColor) // 선택한 색상으로 배경색 설정
//            }
//
//            override fun onCancel(dialog: AmbilWarnaDialog?) {
//                selectedColor = defaultColor // 사용자가 선택을 취소했을 때 기본 색상으로 변경
//                setActivityBackgroundColor(selectedColor) // 기본 색상으로 배경색 설정
//            }
//        })
//        colorPicker.show()
//    }
//
//    // 액티비티의 배경색 설정
//    fun setActivityBackgroundColor(color: Int) {
//        window.decorView.setBackgroundColor(color)
//    }

    fun showColorPickerDialog() {
        val colorPicker = AmbilWarnaDialog(this, selectedColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
            override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                selectedColor = color // 사용자가 선택한 색상 저장
                saveSelectedColor(color) // 선택한 색상 SharedPreferences에 저장
                applyThemeColorToAllActivities(color) // 모든 액티비티에 선택한 색상 적용
            }

            override fun onCancel(dialog: AmbilWarnaDialog?) {
                selectedColor = defaultColor // 사용자가 선택을 취소했을 때 기본 색상으로 변경
                saveSelectedColor(defaultColor) // 기본 색상 SharedPreferences에 저장
                applyThemeColorToAllActivities(defaultColor) // 모든 액티비티에 기본 색상 적용
            }
        })
        colorPicker.show()
    }

    // 선택한 색상을 SharedPreferences에 저장하는 함수
    fun saveSelectedColor(color: Int) {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("selected_color", color)
            apply()
        }
    }

    // 모든 액티비티에 선택한 색상을 적용하는 함수
    fun applyThemeColorToAllActivities(color: Int) {
        // 예시: 액티비티 전환 시 선택한 색상을 적용하는 부분
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selected_color", color)
        startActivity(intent)
    }
}