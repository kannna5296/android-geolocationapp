package com.android.example.geolocationapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var _latitude = 0.0
    private var _longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onMapShowCurrentButtonClick(view: View) {
        val uriStr = "geo:${_latitude},${_longitude}"

        val uri = Uri.parse(uriStr)

        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }

    fun onMapSearchButtonClick(view: View) {
        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)
        val searchWord = URLEncoder.encode(etSearchWord.text.toString(),"UTF-8")

        // "geo"で地図アプリを出す。地図アプリが複数ある場合は選べる
        val uriStr = "geo:0,0%q=${searchWord}"

        val uri = Uri.parse(uriStr)

        val intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }
}