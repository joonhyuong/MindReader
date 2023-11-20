package com.example.calendertest

import android.content.Context
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.calendertest.databinding.ActivityImgBinding
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class ImgActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImgBinding
    private lateinit var galleryImageView: ImageView
    private lateinit var cameraImageView: ImageView
    private lateinit var navigationView: BottomNavigationView


    private var selectedImageUri: Uri? = null // 이미지 Uri를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        galleryImageView = findViewById(R.id.galleryimageView)  // 갤러리 이미지를 보여줄 ImageView
        cameraImageView = findViewById(R.id.cameraimageView)  // 카메라 이미지를 보여줄 ImageView

        navigationView = findViewById(R.id.navigationView)



        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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

        binding.cameraBtn.setOnClickListener{
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activityResultCamera.launch(intent)
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResultGallery.launch(intent)
        }

        binding.galleryBtn2.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageToServer(selectedImageUri!!)
                galleryImageView.setImageURI(selectedImageUri)  // 갤러리에서 선택한 이미지를 보여줄 ImageView에 설정
            } else {
                Toast.makeText(this, "이미지를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val activityResultGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val uri = result.data!!.data
            selectedImageUri = uri // 선택한 이미지의 Uri를 저장
            cameraImageView.setImageURI(selectedImageUri)
//            Glide.with(this)
//                .load(uri)
//                .into(galleryImageView)  // 갤러리에서 선택한 이미지를 보여줄 ImageView에 설정
        }
    }

    private val activityResultCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            selectedImageUri = getImageUri(this, imageBitmap)
            cameraImageView.setImageURI(selectedImageUri)
        }
    }
    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }



    private fun uploadImageToServer(imageUri: Uri) {
        val serverURL = "http://222.102.184.24:80/upload"
        val client = OkHttpClient()

        val realPath = getRealPathFromURI(imageUri)

        if (realPath != null) {
            val file = File(realPath)
            val requestFile = RequestBody.create("image/*".toMediaType(), file)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, requestFile)
                .build()

            val request = Request.Builder()
                .url(serverURL)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "업로드 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "업로드 성공", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ImgActivity, ResultActivity::class.java)
                            startActivity(intent)

                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "업로드 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "실제 파일 경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return it.getString(columnIndex)
        }
        return null
    }
}

