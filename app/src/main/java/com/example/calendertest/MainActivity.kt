package com.example.calendertest

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.calendertest.databinding.ActivityImgBinding
import com.example.calendertest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView
    lateinit var diaryTextView: TextView
    lateinit var imgBtn: Button
    lateinit var textBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView=findViewById(R.id.calendarView)
        diaryTextView=findViewById(R.id.diaryTextView)
        imgBtn=findViewById(R.id.imgBtn)
        textBtn=findViewById(R.id.textBtn)

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

}

class ImgActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImgBinding
    //lateinit var galleryBtn:Button
    //lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_img)
        //galleryBtn=findViewById(R.id.galleryBtn)

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