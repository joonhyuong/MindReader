package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.Manifest
import android.provider.MediaStore
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val PERM_CAMERA = arrayOf(Manifest.permission.CAMERA)
    val PERM_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    val REQ_STORAGE = 99
    val REQ_CAMERA = 100
    val TAKE_CAMERA = 101

    private lateinit var calendar: CalendarView
    private lateinit var tvdate: TextView
    private lateinit var inbtn: Button
    private lateinit var gabtn: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requirePermissions(PERM_STORAGE, REQ_STORAGE)

        binding.btnCamerea.setOnClickListener {
            requirePermissions(PERM_CAMERA, REQ_CAMERA)
        }

        // xml 연결
        calendar = findViewById(R.id.calendar)
        tvdate = findViewById(R.id.tv_date)
        inbtn = findViewById(R.id.ins_btn)
        gabtn = findViewById(R.id.gal_btn)

        // Calendar 클릭 이벤트
        calendar.setOnDateChangeListener { _, year, month, day ->
            tvdate.text = "${year}년 ${month + 1}월 ${day}일 선택"
            inbtn.visibility = View.VISIBLE
            gabtn.visibility = View.VISIBLE
        }

        inbtn.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            startActivity(intent)
        }
    }


    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            REQ_STORAGE -> {
                Toast.makeText(this, "권한이 승인되었습니다", Toast.LENGTH_SHORT).show()
            }
            REQ_CAMERA -> {
                openCamera()
            }
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            REQ_STORAGE -> {
                Toast.makeText(this, "스토리지 권한이 없으면 앱을 실행할 수 없습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
            REQ_CAMERA -> {
                Toast.makeText(this, "카메라 권한이 거절되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, TAKE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                TAKE_CAMERA -> {
                    // 처리할 내용
                }
            }
        }
    }
}
