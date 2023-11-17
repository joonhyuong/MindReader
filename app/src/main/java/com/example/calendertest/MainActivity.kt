package com.example.calendertest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView
    lateinit var diaryTextView: TextView
    lateinit var imgBtn: Button
    lateinit var textBtn: Button
    lateinit var navigationView: BottomNavigationView
    lateinit var contextEditText: EditText
    private val REQUEST_PERMISSIONS=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView=findViewById(R.id.calendarView)
        diaryTextView=findViewById(R.id.diaryTextView)
        navigationView = findViewById(R.id.navigationView)
        imgBtn=findViewById(R.id.imgBtn)
        textBtn=findViewById(R.id.textBtn)
        contextEditText=findViewById(R.id.contextEditText);
        checkPermission()

        calendarView.setOnDateChangeListener {view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            imgBtn.visibility = View.VISIBLE
            textBtn.visibility = View.VISIBLE
            contextEditText.setVisibility(View.VISIBLE);
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.setText("");
        }




        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    // "캘린더" 아이템이 선택되었을 때 처리할 코드를 여기에 추가합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.homeFragment -> {
                    // "히스토리" 아이템을 처리하는 코드를 추가하세요.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myPageFragment -> {
                    // "설정" 아이템을 처리하는 코드를 추가하세요.
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        imgBtn.setOnClickListener{
            val intent = Intent(this, ImgActivity::class.java)
            startActivity(intent)

        }
    }
    private fun checkPermission() {
        val permission = mutableMapOf<String, String>()
        permission["camera"] = Manifest.permission.CAMERA
        permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
        permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE

        // 현재 권한 상태 검사
        val denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

        // 마시멜로 버전 이후
        if(denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission.values.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS) {
            /*권한 요청을 거부했다면 안내 메시지 보여주며 앱 종료*/
            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(applicationContext, "서비스의 필요한 권한입니다.\n권한에 동의해주세요.", Toast.LENGTH_SHORT).show()
                    //finish()
                }
            }
        }
    }

}



