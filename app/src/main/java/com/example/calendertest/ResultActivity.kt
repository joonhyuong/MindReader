package com.example.calendertest

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val queue = Volley.newRequestQueue(this)
        val url = "http://211.248.178.162:33060/get_recommended_books" // 실제 서버 URL로 변경

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                try {
                    // 책 정보 표시를 위한 TextView 가져오기
                    val booksTextView: TextView = findViewById(R.id.booksTextView)
                    var booksText = ""

                    // JSON Array를 순회하며 각 책 정보를 표시
                    for (i in 0 until response.length()) {
                        try {
                            val bookInfo = response.getString(i)
                            // 각 책 정보를 문자열로 합쳐 TextView에 추가
                            booksText += "Book $i: $bookInfo\n\n"
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Log.e("ResultActivity", "Error parsing book at index $i: ${e.message}")
                            // JSON 파싱 에러 처리 코드 추가 (예: 해당 책을 건너뛰거나 오류 메시지 표시)
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