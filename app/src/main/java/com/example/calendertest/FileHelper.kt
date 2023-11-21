package com.example.calendertest

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

object FileHelper {
    fun getFileNamesFromInternalStorage(context: Context): Array<String> {
        val fileList = context.fileList()
        return fileList.filter { it.endsWith("") }.toTypedArray()
    }

    fun getFileContentByName(context: Context, fileName: String): String {
        return try {
            val fileInputStream: FileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }

            stringBuilder.toString()
        } catch (e: IOException) {
            Log.e("Error", "Error reading file: ${e.message}")
            ""
        }
    }
    fun getFirstLineOfFileContent(context: Context, fileName: String): String {
        var firstLine = ""
        try {
            val fileInputStream: FileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            firstLine = bufferedReader.readLine() ?: ""
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()
        } catch (e: IOException) {
            Log.e("Error", "Error reading file: ${e.message}")
        }
        return firstLine
    }

    // 파일의 마지막 줄을 가져오는 함수 (기존 함수 수정)
    fun getSpecificLineOfFileContent(context: Context, fileName: String, lineNumber: Int): String {
        var specificLine = ""
        var lineCount = 0
        try {
            val fileInputStream: FileInputStream = context.openFileInput(fileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                lineCount++
                if (lineCount == lineNumber) {
                    specificLine = line ?: ""
                    break
                }
            }
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()
        } catch (e: IOException) {
            Log.e("Error", "Error reading file: ${e.message}")
        }
        return specificLine
    }




}