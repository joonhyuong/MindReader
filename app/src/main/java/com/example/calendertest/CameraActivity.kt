package com.example.calendertest

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.ByteArrayOutputStream
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import android.widget.Toast
import android.util.Base64

class CameraActivity : BaseActivity() {
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // 객체 생성
        imageView = findViewById(R.id.imageView)
        val picBtn: Button = findViewById(R.id.pic_btn)
        val picBtn2: Button = findViewById(R.id.pic_btn2)

        // 버튼 이벤트
        picBtn.setOnClickListener {
            // 사진 촬영
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResult.launch(intent)
        }

//        picBtn2.setOnClickListener {
//            if (::bitmap.isInitialized) {  // 이미지가 초기화되었는지 확인
//
//                val serverURL = "http://192.168.25.3:5000/upload" // 서버의 업로드 URL
//                val client = OkHttpClient() // OkHttp 클라이언트를 사용
//
//                // 이미지를 Base64로 인코딩
//                val byteArrayOutputStream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//                val byteArray = byteArrayOutputStream.toByteArray()
//                val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
//
//                val mediaType = "application/octet-stream".toMediaType()
//
//                // MultipartBody를 사용하여 폼 데이터 생성
//                val requestBody = MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("image", "uploaded_image.jpg", RequestBody.create(mediaType, base64Image))
//                    .build()
//
//                val request = Request.Builder()
//                    .url(serverURL)
//                    .post(requestBody)
//                    .build()
//
//                client.newCall(request).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        // 업로드 실패 처리
//                        e.printStackTrace()
//                        runOnUiThread {
//                            Toast.makeText(applicationContext, "업로드 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        if (response.isSuccessful) {
//                            // 업로드 성공 처리
//                            runOnUiThread {
//                                Toast.makeText(applicationContext, "성공했습니다", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            // 업로드 실패 처리
//                            runOnUiThread {
//                                Toast.makeText(applicationContext, "업로드 실패", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                })
//            } else {
//                // 이미지가 초기화되지 않은 경우 처리
//                Toast.makeText(applicationContext, "이미지를 먼저 촬영해주세요.", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    // 결과 가져오기
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // 값 담기
            val extras = result.data!!.extras

            // bitmap으로 타입 변경
            bitmap = extras?.get("data") as Bitmap

            // 화면에 보여주기
            imageView.setImageBitmap(bitmap)
        }
    }
}
