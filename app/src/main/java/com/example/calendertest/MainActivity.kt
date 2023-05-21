package com.example.calendertest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.calendertest.databinding.ActivityImgBinding
import com.example.calendertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView
    lateinit var diaryTextView: TextView
    lateinit var imgBtn: Button
    lateinit var textBtn: Button
    private val REQUEST_PERMISSIONS=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView=findViewById(R.id.calendarView)
        diaryTextView=findViewById(R.id.diaryTextView)
        imgBtn=findViewById(R.id.imgBtn)
        textBtn=findViewById(R.id.textBtn)
        checkPermission()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            diaryTextView.visibility = View.VISIBLE
            imgBtn.visibility = View.VISIBLE
            textBtn.visibility = View.VISIBLE
            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
        }

        imgBtn.setOnClickListener({
            val intent = Intent(this, ImgActivity::class.java)
            startActivity(intent)

        })
    }
    private fun checkPermission() {
        var permission = mutableMapOf<String, String>()
        permission["camera"] = Manifest.permission.CAMERA
        permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
        permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE

        // 현재 권한 상태 검사
        var denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

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
                    finish()
                }
            }
        }
    }

}

class ImgActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgBinding.inflate(layoutInflater)
        setContentView(binding.root)

//버튼 이벤트
        binding.galleryBtn.setOnClickListener {
            //갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.cameraBtn.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val uri  = it.data!!.data

            //화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(binding.imageView) //보여줄 위치
        }
    }
}

class CameraActivity : AppCompatActivity() {
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //객체 생성
        imageView = findViewById(R.id.imageView)
        val picBtn: Button = findViewById(R.id.pic_btn)

        //버튼 이벤트
        picBtn.setOnClickListener {
            //사진 촬영
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            activityResult.launch(intent)
        }
    }//onCreate


    //결과 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val extras = it.data!!.extras

            //bitmap으로 타입 변경
            bitmap = extras?.get("data") as Bitmap

            //화면에 보여주기
            imageView.setImageBitmap(bitmap)
        }
    }

}