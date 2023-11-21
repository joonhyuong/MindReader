package com.example.calendertest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

class MemoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)

        // 파일 이름과 파일 내용을 Intent extras로부터 가져옴
        val fileName = intent.getStringExtra("fileName")

        // 파일 내용을 표시하는 TextView를 찾음 (activity_memory.xml에 정의된 TextView id를 사용해야 함)
        val fileContentTextView = findViewById<TextView>(R.id.fileInfoTextView2)

        fileName?.let {
            // 파일 이름을 설정하는 부분
            val fileNameTextView = findViewById<TextView>(R.id.fileNameTextView2)
            fileNameTextView.text = it

            // 파일 내용을 가져와 TextView에 설정
            val fileContent = getFileContentByName(this, it)
            fileContentTextView.text = fileContent
        }
    }

    private fun getFileContentByName(context: Context, fileName: String): String {
        return try {
            val fileInputStream: FileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }

            // 파일 내용을 문자열로 변환하여 반환
            stringBuilder.toString()
        } catch (e: IOException) {
            Log.e("Error", "Error reading file: ${e.message}")
            ""
        }
    }
}




