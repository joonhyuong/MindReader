package com.example.myapplication

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

abstract class BaseActivity : AppCompatActivity() {


    abstract fun permissionGranted(requestConde:Int)
    abstract fun permissionDenied(requestConde:Int)

    //권한 검사
    fun requirePermissions(permission:Array<String>, requestCode:Int) {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            ActivityCompat.requestPermissions(this,permission,requestCode)

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(

        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { it ==PackageManager.PERMISSION_GRANTED} ){
            permissionGranted(requestCode)
        } else{
            permissionDenied(requestCode)
        }
    }
}