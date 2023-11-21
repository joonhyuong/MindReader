package com.example.calendertest

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.EmojiCompatConfigurationView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONException
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        lateinit var selectedDateTextView: TextView
        lateinit var saveBtn: Button
        lateinit var booksTextView: TextView
        selectedDateTextView = findViewById(R.id.selectedDateTextView)
        booksTextView = findViewById(R.id.booksTextView)
        saveBtn = findViewById(R.id.savebtn)
        val queue = Volley.newRequestQueue(this)
        val url = "http://222.102.167.119:5000/get_recommended_books" // 실제 서버 URL로 변경

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                try {
                    // 책 정보 표시를 위한 TextView 가져오기
                    val booksTextView: TextView = findViewById(R.id.booksTextView)
                    var booksText = ""
                    val imageView = findViewById<ImageView>(R.id.bookImageView)

                    for (i in 0 until response.length()) {
                        try {
                            val bookInfo = response.getString(i)
                            when (i) {
                                0 -> booksText += "제목: $bookInfo\n\n "
                                1 -> booksText += "저자: $bookInfo\n\n "
                                2 -> {
                                    val imageUrl = bookInfo
                                    Glide.with(this)
                                        .load(imageUrl)
                                        .into(imageView)
                                }
                                3 -> booksText += "구매링크: $bookInfo\n\n "
                                4 -> booksText += "가격: $bookInfo "
                                5 -> booksText += String.format("%35s\n", "카테고리: $bookInfo\n ")
                                6 -> booksText += "감정: $bookInfo\n\n "
                                7 -> booksText += "글귀 : $bookInfo\n "
                                8 -> booksText += String.format("%50s\n", "-$bookInfo-")
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Log.e("ResultActivity", "Error parsing book at index $i: ${e.message}")
                            // JSON 파싱 에러 처리 코드 추가
                        }
                    }
                    // TextView에 책 정보 문자열 표시
                    booksTextView.text = booksText
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("ResultActivity", "JSON parsing error: ${e.message}")
                    // JSON 파싱 에러 처리 코드 추가
                }
            },
            Response.ErrorListener { error ->
                // 에러가 발생했을 때 처리하는 부분
                Log.e("ResultActivity", "Error: ${error.message}")
                // 에러 처리 코드 추가
            }
        )
        queue.add(jsonArrayRequest)

         fun showDatePickerDialog() {
             val calendar = Calendar.getInstance()
             val year = calendar.get(Calendar.YEAR)
             val month = calendar.get(Calendar.MONTH)
             val day = calendar.get(Calendar.DAY_OF_MONTH)

             fun saveTextToFile(text: String, fileName: String): Boolean {
                 return try {
                     val outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                     outputStream.write(text.toByteArray())
                     outputStream.close()
                     Log.d("ResultActivity", "Text saved to $fileName: $text") // 추가된 로그
                     true // 저장 성공
                 } catch (e: IOException) {
                     e.printStackTrace()
                     Log.e("ResultActivity", "Error saving text to $fileName: ${e.message}")
                     false // 저장 실패
                 }
             }



             fun isValidDate(year: Int, month: Int, day: Int): Boolean {
                 return try {
                     val calendar = Calendar.getInstance()
                     calendar.setLenient(false)
                     calendar.set(year, month, day)
                     true
                 } catch (e: Exception) {
                     false
                 }
             }

             val datePickerDialog = DatePickerDialog(
                 this,
                 { _, selectedYear, selectedMonth, selectedDay ->
                     val isValidDate = isValidDate(selectedYear, selectedMonth, selectedDay)

                     if (isValidDate) {
                         val selectedDate = "$selectedYear 년 ${selectedMonth + 1} 월 $selectedDay 일"
                         selectedDateTextView.text = selectedDate

                         val fileName = "$selectedDate"
                         val textToSave = booksTextView.text.toString()


                         try {
                             saveTextToFile(textToSave, fileName)
                             Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
                         } catch (e: Exception) {
                             e.printStackTrace()
                             Toast.makeText(this, "파일 저장 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                         }
                     } else {
                         Toast.makeText(this, "유효하지 않은 날짜입니다.", Toast.LENGTH_SHORT).show()
                     }
                 },
                 year, month, day
             )
            datePickerDialog.show()


        }

        saveBtn.setOnClickListener {
            showDatePickerDialog()
        }
    }
}
//
//package com.example.calendertest
//
//import android.annotation.SuppressLint
//import android.app.DatePickerDialog
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonArrayRequest
//import com.android.volley.toolbox.Volley
//import com.bumptech.glide.Glide
//import org.json.JSONArray
//import org.json.JSONException
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Locale
//
//class ResultActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
//
//        lateinit var navigationView: BottomNavigationView
//        lateinit var selectedDateTextView: TextView
//        lateinit var saveBtn: Button
//        lateinit var booksTextView: TextView
//        var imageUrl: String? = null
//        navigationView = findViewById(R.id.navigationView)
//        selectedDateTextView = findViewById(R.id.selectedDateTextView)
//        booksTextView = findViewById(R.id.booksTextView)
//        saveBtn = findViewById(R.id.savebtn)
//
//
//        val queue = Volley.newRequestQueue(this)
//        val url = "http://222.102.167.119:5000/get_recommended_books" // 실제 서버 URL로 변경
//
//        navigationView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.calenderFragment -> {
//                    val intent = Intent(this@ResultActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    return@setOnNavigationItemSelectedListener true
//                }
//                R.id.homeFragment -> {
//                    val intent = Intent(this@ResultActivity, HistoryActivity::class.java)
//                    startActivity(intent)
//                    return@setOnNavigationItemSelectedListener true
//                }
//                R.id.myPageFragment -> {
//                    return@setOnNavigationItemSelectedListener true
//                }
//            }
//            false
//        }
//
//        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
//            Response.Listener<JSONArray> { response ->
//                try {
//                    val booksTextView: TextView = findViewById(R.id.booksTextView)
//                    var booksText = ""
//                    val imageView = findViewById<ImageView>(R.id.bookImageView)
//
//                    for (i in 0 until response.length()) {
//                        try {
//                            val bookInfo = response.getString(i)
//                            when (i) {
//                                0 -> booksText += "제목: $bookInfo "
//                                1 -> booksText += String.format("%25s\n","저자: $bookInfo\n ")
//                                2 -> imageUrl = bookInfo
//                                3 -> booksText += "구매링크: $bookInfo\n\n "
//                                4 -> booksText += "가격: $bookInfo "
//                                5 -> booksText += String.format("%35s\n", "카테고리: $bookInfo\n ")
//                                6 -> booksText += "감정: $bookInfo\n\n "
//                                7 -> booksText += "글귀 : $bookInfo\n "
//                                8 -> booksText += String.format("%50s\n", "-$bookInfo-")
//                            }
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("ResultActivity", "Error parsing book at index $i: ${e.message}")
//                            // JSON 파싱 에러 처리 코드 추가
//                        }
//                    }
//                    booksTextView.text = booksText
//                    imageUrl?.let { url ->
//                        val imageView: ImageView = findViewById(R.id.bookImageView)
//                        Glide.with(this@ResultActivity)
//                            .load(url)
//                            .into(imageView)
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    Log.e("ResultActivity", "JSON parsing error: ${e.message}")
//                    // JSON 파싱 에러 처리 코드 추가
//                }
//            },
//            Response.ErrorListener { error ->
//                Log.e("ResultActivity", "Error: ${error.message}")
//                // 에러 처리 코드 추가
//            }
//        )
//        queue.add(jsonArrayRequest)
//
//        // RecyclerView 초기화
//
//
//        fun showDatePickerDialog() {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//            val textToSave = booksTextView.text.toString()
//
//            val datePickerDialog = DatePickerDialog(
//                this,
//                { _, selectedYear, selectedMonth, selectedDay ->
//                    val isValidDate = isValidDate(selectedYear, selectedMonth, selectedDay)
//
//                    if (isValidDate) {
//                        val calendar = Calendar.getInstance()
//                        calendar.set(selectedYear, selectedMonth, selectedDay)
//
//                        val dateFormat = SimpleDateFormat("yyyy 년 MM 월 dd 일", Locale.getDefault())
//                        val formattedDate = dateFormat.format(calendar.time)
//
//                        selectedDateTextView.text = formattedDate
//
//                        val fileName = "$formattedDate"
//                        val textToSave = booksTextView.text.toString()
//
//                        try {
//                            saveTextToFile(textToSave, fileName)
//                            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                            Toast.makeText(this, "파일 저장 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(this, "유효하지 않은 날짜입니다.", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                year, month, day
//            )
//            datePickerDialog.show()
//        }
//
//        saveBtn.setOnClickListener {
//            showDatePickerDialog()
//        }
//    }
//
//    private fun saveTextToFile(text: String, fileName: String): Boolean {
//        return try {
//            val outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
//            outputStream.write(text.toByteArray())
//            outputStream.close()
//            Log.d("ResultActivity", "file namme : $fileName")
//            true
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Log.e("ResultActivity", "Error saving text to $fileName: ${e.message}")
//            false
//        }
//    }
//
//
//    private fun isValidDate(year: Int, month: Int, day: Int): Boolean {
//        return try {
//            val calendar = Calendar.getInstance()
//            calendar.setLenient(false)
//            calendar.set(year, month, day)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//}
//
//
