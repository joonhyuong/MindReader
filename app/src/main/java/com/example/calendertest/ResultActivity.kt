package com.example.calendertest

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.EmojiCompatConfigurationView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONException

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.25.2:5000/get_recommended_books" // 실제 서버 URL로 변경

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
                                0 -> booksText += "제목: $bookInfo\n\n"
                                1 -> booksText += "저자: $bookInfo\n\n"
                                2 -> {
                                    val imageUrl = bookInfo // 이 부분은 실제 URL 가져오는 로직으로 대체되어야 합니다
                                    Glide.with(this)
                                        .load(imageUrl)
                                        .into(imageView)
                                }
                                3 -> booksText += "구매링크: $bookInfo\n\n"
                                4 -> booksText += "가격: $bookInfo\n\n"
                                5 -> booksText += "카테고리: $bookInfo\n\n"
                                6 -> booksText += "감정: $bookInfo\n\n"
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
            })

        queue.add(jsonArrayRequest)
    }
}