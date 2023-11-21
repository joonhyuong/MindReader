package com.example.calendertest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity(), FileNameAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fileNamesFromStorage = FileHelper.getFileNamesFromInternalStorage(this)
        val fileDataList = mutableListOf<Pair<String, String>>() // 파일 이름 및 내용을 담을 리스트

        // 파일 이름 목록을 순회하며 각 파일의 내용을 읽어옴
        fileNamesFromStorage.forEach { fileName ->
            val firstLine = FileHelper.getFirstLineOfFileContent(this, fileName)
            val sixLine = FileHelper.getSpecificLineOfFileContent(this, fileName, 7)
            val fileData = Pair(fileName, sixLine)
            // 파일 이름, 첫 번째 줄, 마지막 줄을 쌍으로 묶어 리스트에 추가
            fileDataList.add(fileData)
        }

        // 파일 내용을 표시하는 어댑터 생성 및 설정
        val fileContentAdapter = FileNameAdapter(fileDataList, this)
        recyclerView.adapter = fileContentAdapter

        val navigationView = findViewById<BottomNavigationView>(R.id.navigationView)

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.homeFragment -> {
                    // Stay in the same activity
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myPageFragment -> {
                    // Handle myPageFragment
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onItemClick(fileName: String, fileContent: String) {
        // Handle item click - Navigate to another activity or perform action
        val intent = Intent(this, MemoryActivity::class.java)
        // Pass necessary data through intent extras if needed
        intent.putExtra("fileName", fileName)
        intent.putExtra("fileContent", fileContent)
        startActivity(intent)
    }
}